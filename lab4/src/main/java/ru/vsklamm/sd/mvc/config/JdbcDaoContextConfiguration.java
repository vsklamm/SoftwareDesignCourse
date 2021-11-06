package ru.vsklamm.sd.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.vsklamm.sd.mvc.dao.TaskJdbcDao;

import javax.sql.DataSource;

@Configuration
@Profile("production")
public class JdbcDaoContextConfiguration {
    @Bean
    public TaskJdbcDao taskJdbcDao(DataSource dataSource) {
        return new TaskJdbcDao(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost/todo");
        dataSource.setUsername("postgres");
        dataSource.setPassword("password");
        return dataSource;
    }
}
