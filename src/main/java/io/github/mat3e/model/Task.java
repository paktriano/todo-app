package io.github.mat3e.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
//@Inheritance(InheritanceType.JOINED);
@Table(name = "tasks")
public class Task{
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int id;
    //    @Column(name = "desc")
    @NotBlank(message = "Tasks description must be not null and must be not be empty")
    private String description;
    private boolean done;

    private LocalDateTime deadline;
//    @Transient // tego pod spodem masz Springu nie zapisywać
    @Embedded
//    @AttributeOverrides({
//                    @AttributeOverride(column = @Column(name = "updatedOn"), name = "updatedOn")
//    })
    private Audit audit = new Audit();

    @ManyToOne // wiele tasków do jednej grupy
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;

//    Task() {
//    }

    protected Task(){
    }

    public Task(String description, LocalDateTime deadline){
        this(description, deadline, null);
    }

    public Task(String description, LocalDateTime deadline, TaskGroup group){
        this.description = description;
        this.deadline = deadline;
        if(group != null){
            this.group = group;
        }
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    TaskGroup getGroup() {
        return group;
    }

    void setGroup(final TaskGroup group) {
        this.group = group;
    }

    public void updateFrom(final Task source){
        description = source.description;
        done = source.done;
        deadline = source.deadline;
        group = source.group;
    }

   }





