package ru.vsklamm.sd.mvc.model;

public class Task {

    public enum Status {
        NOT_STARTED,
        COMPLETED,
    }

    private int taskId;
    private int listId;
    private String description;
    private int status;

    public Task() {
    }

    public Task(final int taskId, final int listId, final String description, final int status) {
        this.taskId = taskId;
        this.listId = listId;
        this.description = description;
        this.status = status;
    }

    public void setCompleted() {
        if (Status.values()[status] == Status.NOT_STARTED) {
            status = Status.COMPLETED.ordinal();
        }
    }

    public String toHtmlText() {
        return "%d. %s".formatted(taskId, description);
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }
}
