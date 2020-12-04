package io.github.mat3e.controller;

import io.github.mat3e.logic.ProjectService;
import io.github.mat3e.model.Project;
import io.github.mat3e.model.ProjectStep;
import io.github.mat3e.model.projection.ProjectWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/projects")
class ProjectController {
    private final ProjectService service;
    private final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    ProjectController(final ProjectService service) {
        this.service = service;
    }

    @GetMapping
    String showProjects(Model model){
//        var projectToEdit = new ProjectWriteModel();
//        projectToEdit.setDescription("Test");
//        model.addAttribute("project", projectToEdit);
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
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

    @PostMapping(value = "/{id}")
    String createGroup(
            @ModelAttribute("project") ProjectWriteModel current,
            Model model,
            @PathVariable("id") int id,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
                    @Valid
                    @NotBlank
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
