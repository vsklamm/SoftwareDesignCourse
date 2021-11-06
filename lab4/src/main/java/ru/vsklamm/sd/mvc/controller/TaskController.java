package ru.vsklamm.sd.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.vsklamm.sd.mvc.dao.TaskDao;
import ru.vsklamm.sd.mvc.model.TaskList;
import ru.vsklamm.sd.mvc.model.Task;

@Controller
public class TaskController {

    private final TaskDao taskDao;

    @Autowired
    public TaskController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @GetMapping("/lists")
    public String getTasksLists(ModelMap map) {
        map.addAttribute("taskLists", taskDao.getTaskLists());
        map.addAttribute("taskList", new TaskList());
        map.addAttribute("task", new Task());
        return "index";
    }

    @PostMapping("/add-task")
    public String addTask(@ModelAttribute("task") Task task) {
        if (validateTask(task)) {
            taskDao.addTask(task);
        }
        return "redirect:/lists";
    }

    @PostMapping("/add-list")
    public String addList(@ModelAttribute("taskList") TaskList taskList) {
        if (validateTaskList(taskList)) {
            taskDao.addList(taskList);
        }
        return "redirect:/lists";
    }

    @PostMapping("/task-completed")
    public String markAsCompleted(@RequestParam(name = "taskId") int taskId) {
        taskDao.markAsCompleted(taskId);
        return "redirect:/lists";
    }

    @PostMapping("/delete-task")
    public String deleteTask(@RequestParam(name = "taskId") int taskId) {
        taskDao.deleteTask(taskId);
        return "redirect:/lists";
    }

    @PostMapping("/delete-list")
    public String deleteList(@RequestParam(name = "listId") int listId) {
        taskDao.deleteList(listId);
        return "redirect:/lists";
    }

    boolean validateTask(final Task task) {
        return !task.getDescription().isEmpty();
    }

    boolean validateTaskList(final TaskList taskList) {
        return !taskList.getName().isEmpty();
    }
}
