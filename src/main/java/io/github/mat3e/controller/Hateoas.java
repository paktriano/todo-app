package io.github.mat3e.controller;


import io.github.mat3e.model.Task;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

class Hateoas extends RepresentationModel {
    List<Task> tasks;

    Hateoas(List<Task> tasks){
        this.tasks = tasks;
    }


    List<Task> getTasks() {
        return tasks;
    }
}
