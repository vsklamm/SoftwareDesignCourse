package ru.vsklamm.sd.mvc.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TaskList {
    private int id;
    private String name;
    private List<Task> tasks = new ArrayList<>();

    public TaskList() {
    }

    public String toHtmlText() {
        return "%d. %s".formatted(id, name);
    }

    public void addTask(final Task task) {
        tasks.add(task);
    }
}
