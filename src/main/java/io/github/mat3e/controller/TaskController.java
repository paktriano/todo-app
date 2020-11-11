package io.github.mat3e.controller;

import io.github.mat3e.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;

@RepositoryRestController
class TaskController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }

    ResponseEntity<?> readAllTasks(){
        LOGGER.warn("Exposing all the task");
        return ResponseEntity.ok(repository.findAll());
    }
}
