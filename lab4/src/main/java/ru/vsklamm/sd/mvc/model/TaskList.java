package ru.vsklamm.sd.mvc.model;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private int id;
    private String name;
    private List<Task> tasks = new ArrayList<>();

    public TaskList() {
    }

    public TaskList(final int id, final String name, List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.tasks = tasks;
    }

    public String toHtmlText() {
        return "%d. %s".formatted(id, name);
    }

    public void addTask(final Task task) {
        tasks.add(task);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

}
