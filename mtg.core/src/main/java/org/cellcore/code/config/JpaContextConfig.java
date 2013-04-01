package org.cellcore.code.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.cellcore.code.dao.DaoMarker;
import org.cellcore.code.model.AbstractJPAEntity;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 *
 * Copyright 2013 Freddy Munoz (freddy@cellcore.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ==============================================================
 * Spring JPA configuration (backed with hibernate 4)
 */
@Configuration
/**
 * enable transaction management using the transaction manager defined here
 */
@EnableTransactionManagement
/**
 * continue to use property files
 */
@PropertySource("classpath:appConfig.properties")
/**
 * use a placeholder marker instead of a string to define the packages to scan for components
 *
 */
@ComponentScan(
        basePackageClasses = {DaoMarker.class}
)
public class JpaContextConfig implements TransactionManagementConfigurer {

    @Autowired
    Environment environment;

    /**
     * database configuration settings
     *
     * @return
     */
    @Bean
    public DataSource dataSource() {
        /**
         * create a basic database connection
         */
        final BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(environment.getProperty("db.driver"));
        ds.setUrl(environment.getProperty("db.url"));
        ds.setUsername(environment.getProperty("db.username"));
        ds.setPassword(environment.getProperty("db.password"));
        return ds;
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return transactionManager();
    }


    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory());
        transactionManager.setDataSource(dataSource());
        transactionManager.setJpaDialect(new HibernateJpaDialect());
        return transactionManager;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        /**
         * no string package dependency, instead use a placeholder.
         */
        entityManagerFactoryBean.setPackagesToScan(AbstractJPAEntity.class.getPackage().getName());
        entityManagerFactoryBean.setDataSource(dataSource());

        /**
         * tell hibernate to either update, verify or create (create-drop) the database schema
         */
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabasePlatform(environment.getProperty("db.dialect"));
        jpaVendorAdapter.setShowSql(environment.getProperty("db.showSql", Boolean.class, false));
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

        entityManagerFactoryBean.getJpaPropertyMap().put("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
        entityManagerFactoryBean.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", environment.getProperty("db.ddl"));

        /**
         * lines specific to hibernate
         */
        entityManagerFactoryBean.setPersistenceProvider(new HibernatePersistence());

        entityManagerFactoryBean.afterPropertiesSet();

        return entityManagerFactoryBean.getObject();
    }

}
