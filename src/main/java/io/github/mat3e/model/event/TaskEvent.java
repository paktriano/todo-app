package io.github.mat3e.model.event;


import io.github.mat3e.model.Task;

import java.time.Clock;
import java.time.Instant;

public abstract class TaskEvent {
    private int taskId;
    private Instant occurrence;

    public static TaskEvent changed(Task source){
       return source.isDone() ? new TaskDone(source) : new TaskUndone(source);
    }


    protected TaskEvent(final int taskId, Clock clock) {
        this.taskId = taskId;
        this.occurrence = Instant.now();
    }


    public int getTaskId() {
        return taskId;
    }

    public Instant getOccurrence() {
        return occurrence;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+ "{" +
                "taskId=" + taskId + ", occurrence=" + occurrence +
                "}";
    }
}
