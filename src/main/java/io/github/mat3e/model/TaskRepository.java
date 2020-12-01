package io.github.mat3e.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();

    Page<Task> findAll(Pageable page);

    Optional<Task> findById(Integer id);

    boolean existsById(Integer id);

    Task save(Task entity);

    List<Task> findByDone(boolean done);

//    @RequestMapping(path = "tasks/search/done")
//    @RestResource(path = "/tasks/done", rel = "done")

//    List<Task> findByDone(@Param("state") boolean done);

    void flush();

    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    List<Task> findAllByGroup_Id(Integer groupId);

    List<Task> findTasksByDoneIsFalse();

}
