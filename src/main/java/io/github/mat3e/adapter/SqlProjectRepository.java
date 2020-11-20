package io.github.mat3e.adapter;

import io.github.mat3e.model.Project;
import io.github.mat3e.model.ProjectRepository;
import io.github.mat3e.model.TaskGroup;
import io.github.mat3e.model.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {

    @Override
    @Query("from Project p join fetch p.steps")
    List<Project> findAll();

//    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);
}
