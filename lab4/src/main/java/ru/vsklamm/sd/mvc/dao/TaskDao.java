package ru.vsklamm.sd.mvc.dao;

import ru.vsklamm.sd.mvc.model.Task;
import ru.vsklamm.sd.mvc.model.TaskList;

import java.util.List;

public interface TaskDao {
    List<TaskList> getTaskLists();

    void addTask(Task task);

    void addList(TaskList list);

    void markAsCompleted(int taskId);

    void deleteTask(int taskId);

    void deleteList(int listId);
}
