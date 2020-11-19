package io.github.mat3e.adapter;

import io.github.mat3e.model.Task;
import io.github.mat3e.model.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {
//    List<Task> findByDone(@Param("state") boolean done);

    @Override
    @Query(nativeQuery = true, value = "SELECT count(*) > 0 from TASKS where ID=:id") // argument z metody nr 2
    boolean existsById(@Param("id") Integer id);

    @Override
    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

}
