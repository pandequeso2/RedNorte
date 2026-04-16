package cl.RedNorte.Backend.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    // FIX: el paquete real es .notificacion (sin "es")
    basePackages = "cl.RedNorte.Backend.repository.notificacion",
    entityManagerFactoryRef = "notificacionesEntityManagerFactory",
    transactionManagerRef = "notificacionesTransactionManager"
)
public class NotificacionesDataSourceConfig {

    @Bean(name = "notificacionesDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.notificaciones")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "notificacionesEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("notificacionesDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("cl.RedNorte.Backend.model.notificaciones")
                .persistenceUnit("notificaciones")
                .build();
    }

    @Bean(name = "notificacionesTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("notificacionesEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}