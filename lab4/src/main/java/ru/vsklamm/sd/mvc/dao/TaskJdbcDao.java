package ru.vsklamm.sd.mvc.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import ru.vsklamm.sd.mvc.model.Task;
import ru.vsklamm.sd.mvc.model.TaskList;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

public class TaskJdbcDao extends JdbcDaoSupport implements TaskDao {

    public TaskJdbcDao(DataSource dataSource) {
        super();
        setDataSource(dataSource);
    }

    @Override
    public List<TaskList> getTaskLists() {
        final String sql = "SELECT task_id, list_id, description, status FROM Tasks WHERE list_id = ";
        return getJdbcTemplate().queryForList("SELECT list_id, name FROM TasksLists ORDER BY list_id")
                .stream().map(
                        mp -> {
                            final var listId = (int) mp.get("list_id");
                            final var name = (String) mp.get("name");
                            final List<Task> tasks = getJdbcTemplate().query(sql + listId, new BeanPropertyRowMapper<>(Task.class));
                            return new TaskList(listId, name, tasks);
                        }
                ).collect(Collectors.toList());
    }

    @Override
    public void addTask(Task task) {
        // TODO: check for existence
        final String sql = "INSERT INTO Tasks (list_id, description, status) VALUES (?, ?, ?)";
        getJdbcTemplate().update(sql, task.getListId(), task.getDescription(), 0);
    }

    @Override
    public void addList(TaskList list) {
        final String sql = "INSERT INTO TasksLists (name) VALUES (?)";
        getJdbcTemplate().update(sql, list.getName());
    }

    @Override
    public void markAsCompleted(int taskId) {
        final String sql = "UPDATE Tasks SET status = 1 WHERE task_id = ?";
        getJdbcTemplate().update(sql, taskId);
    }

    @Override
    public void deleteTask(int taskId) {
        final String sql = "DELETE FROM Tasks WHERE task_id = ?";
        getJdbcTemplate().update(sql, taskId);
    }

    @Override
    public void deleteList(int listId) {
        getJdbcTemplate().update("DELETE FROM Tasks WHERE list_id = ?", listId);
        getJdbcTemplate().update("DELETE FROM TasksLists WHERE list_id = ?", listId);
    }
}
