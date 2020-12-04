package io.github.mat3e.logic;

import io.github.mat3e.model.Task;
import io.github.mat3e.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TaskService {
    private final TaskRepository REPOSITORY;
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    public TaskService(final TaskRepository repository) {
        REPOSITORY = repository;
    }

    @Async
    public CompletableFuture<List<Task>> findAllAsync(){
        LOGGER.info("Supply Async");
        return CompletableFuture.supplyAsync(REPOSITORY::findAll);
    }

}
