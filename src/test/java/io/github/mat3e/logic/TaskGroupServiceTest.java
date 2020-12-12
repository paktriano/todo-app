package io.github.mat3e.logic;

import io.github.mat3e.model.TaskGroup;
import io.github.mat3e.model.TaskGroupRepository;
import io.github.mat3e.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when group has undone tasks")
    void toggleGroup_taskGroup_hasUndoneTasks_throwsIllegalStateException() {
        // given
        var taskRepository= mock(TaskRepository.class);
        when(taskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(true);

        // system to test
        var toTest = new TaskGroupService(null, taskRepository) ;

        // when
        var exception = catchThrowable(()-> toTest.toggleGroup(anyInt()));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Group has undone tasks.");

    }

    @Test
    @DisplayName("should throw IllegalArgumentException when group is not found by id")
    void toggleGroup_taskGroup_hasNotUndoneTasks_taskGroupNotFoundById_throwsIllegalArgumentException() {
        // given
        var taskRepository= mock(TaskRepository.class);
        when(taskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);
        // and
        var taskGroupRepository = mock(TaskGroupRepository.class);
        when(taskGroupRepository.findById(anyInt())).thenReturn(Optional.empty());

        // system under test
        var toTest = new TaskGroupService(taskGroupRepository, taskRepository);

        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(anyInt()));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("TaskGroup with given id not found");
    }

    @Test
    @DisplayName("should toggle group")
    void toggleGroup_worksAsExpected() {
        // given
        var taskRepository= mock(TaskRepository.class);
        when(taskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);
        // and
        var group = new TaskGroup();
        var beforeToggle = group.isDone();
        // and
        var taskGroupRepository = mock(TaskGroupRepository.class);

        when(taskGroupRepository.findById(anyInt())).thenReturn(Optional.of(group));

        // system under test
        var toTest = new TaskGroupService(taskGroupRepository, taskRepository);

        // when
       toTest.toggleGroup(0);

        // then
        assertThat(group.isDone()).isEqualTo(!beforeToggle);
    }


    @Test
    @DisplayName("group has not undone tasks, taskGroup found by id, done should be changed")
    void toggleGroup_taskGroup_hasNotUndoneTasks_taskGroupFoundById_doneStateChanged() {
        // given
        int index = 0;
        var taskRepository= mock(TaskRepository.class);
        when(taskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);
        // and
        var taskGroupRepository = mock(TaskGroupRepository.class);

        var taskGroup = new TaskGroup();
        taskGroupRepository.save(taskGroup);
        // and

       when(taskGroupRepository.findById(anyInt())).thenReturn(Optional.of(taskGroup));

        // system under test
        var toTest = new TaskGroupService(taskGroupRepository, taskRepository);

        // when
        var beforeToggle = taskGroupRepository.findById(taskGroup.getId()).get().isDone();
        System.out.println(beforeToggle);
        toTest.toggleGroup(taskGroup.getId());
        var afterToggle = taskGroupRepository.findById(taskGroup.getId()).get().isDone();
        System.out.println(afterToggle);
        // then
        assertTrue(beforeToggle != afterToggle);

    }
}