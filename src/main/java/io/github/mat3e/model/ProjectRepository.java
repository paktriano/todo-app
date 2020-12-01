package io.github.mat3e.model;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface ProjectRepository {
    List<Project> findAll();

    Optional<Project> findById(Integer id);

    Project save(Project entity);
}
