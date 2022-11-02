package by.modsen.meetup.config;

import by.modsen.meetup.exceptions.DriverRuntimeException;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DaoConfig {

    private final Environment env;

    public DaoConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("by.modsen.meetup.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource pool = new ComboPooledDataSource();
        try {
            pool.setDriverClass(env.getProperty("spring.datasource.driver-class-name"));
        } catch (PropertyVetoException e) {
            throw new DriverRuntimeException("Check driver name", e);
        }

        pool.setJdbcUrl(env.getProperty("spring.datasource.url"));
        pool.setUser(env.getProperty("spring.datasource.username"));
        pool.setPassword(env.getProperty("spring.datasource.password"));

        return pool;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager
                = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty(
                "hibernate.format_sql", env.getProperty("spring.hibernate.format_sql", "false"));
        hibernateProperties.setProperty(
                "hibernate.show_sql", env.getProperty("spring.hibernate.show_sql", "false"));
        hibernateProperties.setProperty(
                "hibernate.hbm2ddl.auto", env.getProperty("spring.hibernate.hbm2ddl.auto", "none"));

        return hibernateProperties;
    }
}