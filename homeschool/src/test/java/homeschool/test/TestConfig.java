package homeschool.test;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.validation.Validator;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import homeschool.profiler.Profiler;
import homeschool.ui.Resources;

@Configuration
@EnableSpringConfigured
@ComponentScan(basePackages = {"homeschool.service", "homeschool.test"})
@EnableJpaRepositories(basePackages = {"homeschool.repository"})
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@EnableMBeanExport
@PropertySource("classpath:/test.config.properties")
public class TestConfig {
    @Autowired
    private Environment env;

    @Bean
    public Map<String, Object> jpaPropertyMap() {
        Map<String, Object> jpaPropertyMap = new HashMap<>();
        jpaPropertyMap.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        jpaPropertyMap.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.generate.ddl"));
        jpaPropertyMap.put("hibernate.show_sql", env.getProperty("hibernate.show.sql"));
        jpaPropertyMap.put("hibernate.format_sql", env.getProperty("hibernate.format.sql"));
        jpaPropertyMap.put("hibernate.cache.use_second_level_cache", env.getProperty("hibernate.cache.use.second.level.cache"));
        jpaPropertyMap.put("hibernate.cache.use_query_cache", env.getProperty("hibernate.cache.use.query.cache"));
        jpaPropertyMap.put("hibernate.cache.region.factory_class", env.getProperty("hibernate.cache.region.factory.class"));
        jpaPropertyMap.put("hibernate.generate_statistics", env.getProperty("hibernate.generate.statistics"));
        jpaPropertyMap.put("javax.persistence.sharedCache.mode", "ENABLE_SELECTIVE");
        jpaPropertyMap.put("javax.persistence.validation.mode", "AUTO");
        return jpaPropertyMap;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));
        dataSource.setInitialSize(Integer.parseInt(env.getProperty("db.initial.size")));
        dataSource.setMaxActive(Integer.parseInt(env.getProperty("db.max.active")));
        dataSource.setDefaultAutoCommit(false);
        return dataSource;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setDataSource(dataSource());
        factory.setPackagesToScan("homeschool.entity");
        factory.setJpaPropertyMap(jpaPropertyMap());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }

    @Bean
    public PersistenceExceptionTranslator persistenceExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public Validator validator() {
        final LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(resources());
        return validator;
    }

    @Bean
    public Resources resources() {
        return new Resources();
    }

    @Bean
    public Profiler profiler() {
        return new Profiler();
    }
}