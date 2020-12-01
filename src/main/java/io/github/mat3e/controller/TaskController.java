package io.github.mat3e.controller;

import io.github.mat3e.logic.TaskService;
import io.github.mat3e.model.Task;
import io.github.mat3e.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;


//@RepositoryRestController
@RestController
@RequestMapping("/tasks")
class TaskController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;
    private final TaskService service;

    TaskController(final TaskRepository repository, final TaskService service) {
        this.repository = repository;
        this.service = service;
    }

//    @RequestMapping(method = RequestMethod.GET, path = "/tasks")
//    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"})
    @GetMapping(params = {"!sort", "!page", "!size"})
    CompletableFuture<ResponseEntity<List<Task>>> readAllTasks(){
        LOGGER.warn("Exposing all the task");
        return service.findAllAsync().thenApply(ResponseEntity::ok);
    }

//    @GetMapping("/tasks")
    @GetMapping
    ResponseEntity<List<Task>> readAllTasks(Pageable page){
        LOGGER.info("Custom pager");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

//    @GetMapping("/tasks/{id}")
    @GetMapping("/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id){
        LOGGER.warn("One task is reading");

        return repository.findById(id)
                .map(task ->ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());

    }
//    @PutMapping("/tasks/{id}")
    @PutMapping("/{id}")
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
//    @PatchMapping("/tasks/{id}")
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable("id") int id){
        if(!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.findById(id).ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }

//    @PostMapping("/tasks")
    @PostMapping
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate) {
        Task result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping(value = "/search/done", params = "state")
    ResponseEntity<List<Task>> findByDone(Boolean state){
        return ResponseEntity.ok(repository.findByDone(state));
    }

    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state){
        return ResponseEntity.ok(
                repository.findByDone(state)
        );
    }

//    @RequestMapping(method = RequestMethod.GET, path = "/search/today")
//    ResponseEntity<List<Task>> readTasksForToday(){
//        var undoneTasks = repository.findTasksByDoneIsFalse();
//
//    }

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
