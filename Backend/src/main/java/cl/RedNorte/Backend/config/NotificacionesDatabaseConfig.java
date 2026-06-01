package cl.RedNorte.Backend.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "notificacionesEntityManagerFactory",
        transactionManagerRef = "notificacionesTransactionManager",
        basePackages = {"cl.RedNorte.Backend.repository.notificacion"}
)
public class NotificacionesDatabaseConfig {

    @Bean(name = "notificacionesDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.notificaciones")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "notificacionesEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean notificacionesEntityManagerFactory(
            @Qualifier("notificacionesDataSource") DataSource dataSource) {
        
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("cl.RedNorte.Backend.model.notificaciones");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.show_sql", "true");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "notificacionesTransactionManager")
    public PlatformTransactionManager notificacionesTransactionManager(
            @Qualifier("notificacionesEntityManagerFactory") EntityManagerFactory notificacionesEntityManagerFactory) {
        return new JpaTransactionManager(notificacionesEntityManagerFactory);
    }
}
