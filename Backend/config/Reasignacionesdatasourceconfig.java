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
    // FIX: el paquete real es .reasignacion (sin "es")
    basePackages = "cl.RedNorte.Backend.repository.reasignacion",
    entityManagerFactoryRef = "reasignacionesEntityManagerFactory",
    transactionManagerRef = "reasignacionesTransactionManager"
)
public class ReasignacionesDataSourceConfig {

    @Bean(name = "reasignacionesDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.reasignaciones")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "reasignacionesEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("reasignacionesDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("cl.RedNorte.Backend.model.reasignaciones")
                .persistenceUnit("reasignaciones")
                .build();
    }

    @Bean(name = "reasignacionesTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("reasignacionesEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}