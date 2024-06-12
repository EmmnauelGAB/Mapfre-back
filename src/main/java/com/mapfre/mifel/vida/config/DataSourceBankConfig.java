package com.mapfre.mifel.vida.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "bankEntityManagerFactory",
        transactionManagerRef = "bankTransactionManager",
        basePackages = { "com.mapfre.mifel.vida.repository"})
@PropertySource("classpath:application.properties")
public class DataSourceBankConfig {

    private static final String URL="spring.datasource.url";
    private static final String UNAME="spring.datasource.username";
    private static final String PNAME="spring.datasource.password";
    private static final String DRIVER="spring.datasource.driver-class-name";
    private static final String DDL="spring.jpa.hibernate.ddl-auto";
    private static final String SHOW="spring.jpa.show-sql";
    private static final String DIALECT="spring.jpa.database-platform";

    private static final String PACKAGE_ENTITY="com.mapfre.mifel.vida.entity";

    @Autowired
    private Environment envBank; // Contains Properties Load by @PropertySources

    @Bean(name = "bankDataSource")
    public DataSource bankDatasource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(envBank.getProperty(URL));
        dataSource.setUsername(envBank.getProperty(UNAME));
        dataSource.setPassword(envBank.getProperty(PNAME));
        dataSource.setDriverClassName(envBank.getProperty(DRIVER));
        return dataSource;
    }

    @Bean(name = "bankEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(bankDatasource());
        em.setPackagesToScan(PACKAGE_ENTITY);

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", envBank.getProperty(DDL));
        properties.put("hibernate.show-sql", envBank.getProperty(SHOW));
        properties.put("hibernate.dialect", envBank.getProperty(DIALECT));
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean(name = "bankTransactionManager")
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}