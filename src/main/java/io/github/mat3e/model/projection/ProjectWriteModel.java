package io.github.mat3e.model.projection;

import io.github.mat3e.model.Project;
import io.github.mat3e.model.ProjectStep;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProjectWriteModel {
    @NotBlank(message = "Step description must be not null and must be not be empty")
    private String description;
    @Valid
    private List<ProjectStep> steps = new ArrayList<>();
    private int id;

    public ProjectWriteModel(){
        steps.add(new ProjectStep());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<ProjectStep> getSteps() {
        return steps;
    }

    public void setSteps(final List<ProjectStep> steps) {
        this.steps = steps;
    }

    public Project toProject(){
        var result = new Project();
        result.setDescription(description);
        steps.forEach(step -> step.setProject(result));
        result.setSteps(new HashSet<>(steps));
        return result;
    }

    public int getId() {
        return id;
    }

    void setId(final int id) {
        this.id = id;
    }
}
