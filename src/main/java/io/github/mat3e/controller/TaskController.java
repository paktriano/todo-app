package io.github.mat3e.controller;

import io.github.mat3e.model.Task;
import io.github.mat3e.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;


//@RepositoryRestController
@Controller
class TaskController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;


    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }

//    @RequestMapping(method = RequestMethod.GET, path = "/tasks")
    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks(){
        LOGGER.warn("Exposing all the task");

        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page){
        LOGGER.info("Custom pager");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping("/tasks/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id){
        LOGGER.warn("One task is reading");

        return repository.findById(id)
                .map(task ->ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());

    }
    @PutMapping("/tasks/{id}")
    ResponseEntity<?> updateTask(@PathVariable("id") int id, @RequestBody @Valid Task toUpdate){
        if(!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id).ifPresent(task -> {
            task.setDone(!task.isDone());
            task.updateFrom(toUpdate);
            repository.save(task);
        });
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PatchMapping("/tasks/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable("id") int id){
        if(!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.findById(id).ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/tasks")
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate) {
        Task result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping(value = "tasks/search/done", params = "state")
    ResponseEntity<List<Task>> findByDone(Boolean state){
        return ResponseEntity.ok(repository.findByDone(state));
    }

//    @GetMapping(value = "tasks/search/done", params = "state")
//    @RequestMapping(value = "tasks/search/done", params = "state")
//    ResponseEntity<?> findByDone(Boolean state){
//        Hateoas resources = new Hateoas(repository.findByDone(state));
//
//        resources.add(linkTo(methodOn(Hateoas.class).
//        resources.add(linkTo(methodOn(Hateoas.class).getTasks()).withSelfRel());
//        resources.add(resources.getTasks());
//        return ResponseEntity.ok(resources);
//        return ResponseEntity.ok(repository.findByDone(state));
//    }
}
