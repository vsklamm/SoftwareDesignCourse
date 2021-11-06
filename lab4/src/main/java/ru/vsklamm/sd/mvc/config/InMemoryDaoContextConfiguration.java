package ru.vsklamm.sd.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.vsklamm.sd.mvc.dao.TaskDao;
import ru.vsklamm.sd.mvc.dao.TaskInMemoryDao;

@Configuration
@Profile("testing")
public class InMemoryDaoContextConfiguration {
    @Bean
    public TaskDao taskDao() {
        return new TaskInMemoryDao();
    }
}
