package io.github.mat3e.controller;

import io.github.mat3e.logic.ProjectService;
import io.github.mat3e.model.Project;
import io.github.mat3e.model.ProjectStep;
import io.github.mat3e.model.projection.ProjectWriteModel;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
//@PreAuthorize("hasRole('ROLE_EDWIN')")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/projects")
@IllegalExceptionProcessing
class ProjectController {
    private final ProjectService service;
    private final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    ProjectController(final ProjectService service) {
        this.service = service;
    }

    @GetMapping
    String showProjects(Model model, Authentication auth, Principal p){
//        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("project", new ProjectWriteModel());
            return "projects";
//        }
//        return "index";

//        var projectToEdit = new ProjectWriteModel();
//        projectToEdit.setDescription("Test");
//        model.addAttribute("project", projectToEdit);
//        model.addAttribute("project", new ProjectWriteModel());
//        return "index";
    }

    @PostMapping
    String addProject(
            @ModelAttribute("project") @Valid ProjectWriteModel current,
            BindingResult bindingResult, //sprawdza czy poprzedni argument zawiera błędy
            Model model
    ){
        if (bindingResult.hasErrors()){
            return "projects";
        }
        service.save(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("projects", getProjects());
        model.addAttribute("message", "Dodano projekt!");
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current){
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @PostMapping(value = "/fake/{id}")
    String createGroupFake (
            @ModelAttribute("project") ProjectWriteModel current,
            Model model,
            @PathVariable("id") int id,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
                    LocalDateTime deadline
    ) {
        return createGroup(current, model, id, deadline);
    }

    @Timed(value = "project.create.group", histogram = true, percentiles = {0.5, 0.95, 0.99})
    @PostMapping(value = "/{id}")
    String createGroup (
            @ModelAttribute("project") ProjectWriteModel current,
            Model model,
            @PathVariable("id") int id,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
                    LocalDateTime deadline
    ){
        try {
            service.createGroup(deadline,id);
            model.addAttribute("message", "Dodano grupę!");
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("message", "Wystąpił błąd podczas tworzenia grupy");
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (Exception e2){
            e2.printStackTrace();
            logger.error(e2.getMessage());
        }

        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects(){
        return service.readAll();
    }


}
