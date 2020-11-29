package io.github.mat3e.controller;

import io.github.mat3e.logic.TaskGroupService;

import io.github.mat3e.model.TaskRepository;
import io.github.mat3e.model.projection.GroupReadModel;
import io.github.mat3e.model.projection.GroupWriteModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
class TaskGroupController {
    private final TaskGroupService groupService;
    private final TaskRepository taskRepository;


    TaskGroupController(final TaskGroupService groupService, final TaskRepository taskRepository) {
        this.groupService = groupService;
        this.taskRepository = taskRepository;
//        this.groupRepository = groupRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createGroup(@RequestBody @Valid GroupWriteModel newTaskGroup){
        GroupReadModel result = groupService.createGroup(newTaskGroup);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<GroupReadModel>> readAllTasks(){
        return ResponseEntity.ok(groupService.readAll());
    }

    @RequestMapping(method = RequestMethod.PATCH, path = "/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable("id") int id){
        groupService.toggleGroup(id);
        return ResponseEntity.ok(groupService.readAll().stream().filter(group -> { return group.getId() == id;}));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/tasks")
    public ResponseEntity<?> readTasksFromGroup(@PathVariable("id") int id){
        return taskRepository.findAllByGroup_Id(id)
                .map(tasks -> ResponseEntity.ok(tasks))
                .orElse(ResponseEntity.notFound().build());
    }

}
