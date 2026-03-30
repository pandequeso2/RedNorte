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
    basePackages = "cl.RedNorte.Backend.repository.espera",
    entityManagerFactoryRef = "esperaEntityManagerFactory",
    transactionManagerRef = "esperaTransactionManager"
)
public class EsperaDataSourceConfig {

    @Bean(name = "esperaDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.espera")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "esperaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("esperaDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("cl.RedNorte.Backend.model.espera")
                .persistenceUnit("espera")
                .build();
    }

    @Bean(name = "esperaTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("esperaEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}