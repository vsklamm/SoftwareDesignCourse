package ru.vsklamm.sd.mvc.dao;

import ru.vsklamm.sd.mvc.model.Task;
import ru.vsklamm.sd.mvc.model.TaskList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskInMemoryDao implements TaskDao {
    private final AtomicInteger lastListId = new AtomicInteger(0);
    private final AtomicInteger lastTaskId = new AtomicInteger(0);

    private final List<TaskList> taskLists = new ArrayList<>();

    @Override
    public List<TaskList> getTaskLists() {
        return taskLists;
    }

    @Override
    public void addTask(Task task) {
        if (taskLists.stream().noneMatch(l -> l.getId() == task.getListId()) ||
                taskLists.stream().anyMatch(l -> l.getTasks().stream().anyMatch(t -> t.getTaskId() == task.getTaskId()))) {
            return;
        }
        final var newId = lastTaskId.incrementAndGet();
        task.setTaskId(newId);
        taskLists.stream()
                .filter(l -> l.getId() == task.getListId())
                .forEach(tl -> tl.addTask(task));
    }

    @Override
    public void addList(TaskList taskList) {
        if (taskLists.stream().anyMatch(l -> l.getId() == taskList.getId())) {
            return;
        }
        final var id = lastListId.incrementAndGet();
        taskList.setId(id);
        taskLists.add(taskList);
    }

    @Override
    public void markAsCompleted(int taskId) {
        if (taskLists.stream().noneMatch(l -> l.getTasks().stream().anyMatch(t -> t.getTaskId() == taskId))) {
            return;
        }
        taskLists.forEach(l -> l.getTasks().stream()
                .filter(t -> t.getTaskId() == taskId)
                .forEach(Task::setCompleted));
    }

    @Override
    public void deleteTask(int taskId) {
        if (taskLists.stream().noneMatch(l -> l.getTasks().stream().anyMatch(t -> t.getTaskId() == taskId))) {
            return;
        }
        taskLists.forEach(l -> l.getTasks().removeIf(t -> t.getTaskId() == taskId));
    }

    @Override
    public void deleteList(int listId) {
        taskLists.removeIf(l -> l.getId() == listId);
    }
}
