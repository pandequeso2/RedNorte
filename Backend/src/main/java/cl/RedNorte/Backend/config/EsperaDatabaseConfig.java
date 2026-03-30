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
        entityManagerFactoryRef = "esperaEntityManagerFactory",
        transactionManagerRef = "esperaTransactionManager",
        basePackages = {"cl.RedNorte.Backend.repository.espera"}
)
public class EsperaDatabaseConfig {

    @Bean(name = "esperaDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.espera")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "esperaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean esperaEntityManagerFactory(
            @Qualifier("esperaDataSource") DataSource dataSource) {
        
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("cl.RedNorte.Backend.model.espera");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.show_sql", "true");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "esperaTransactionManager")
    public PlatformTransactionManager esperaTransactionManager(
            @Qualifier("esperaEntityManagerFactory") EntityManagerFactory esperaEntityManagerFactory) {
        return new JpaTransactionManager(esperaEntityManagerFactory);
    }
}
