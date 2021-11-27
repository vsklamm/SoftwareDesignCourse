package ru.vsklamm.sd.mvc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
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

    public void setCompleted() {
        if (Status.values()[status] == Status.NOT_STARTED) {
            status = Status.COMPLETED.ordinal();
        }
    }

    public String toHtmlText() {
        return "%d. %s".formatted(taskId, description);
    }
}
