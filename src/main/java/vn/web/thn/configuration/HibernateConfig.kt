package vn.web.thn.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.context.ApplicationContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.DriverManagerDataSource

import org.springframework.orm.hibernate5.HibernateTransactionManager
import org.springframework.orm.hibernate5.LocalSessionFactoryBean
import java.util.*
import javax.sql.DataSource

@PropertySource("classpath:ds-hibernate-cfg.properties")
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = arrayOf( "vn.web.thn" ))
open class HibernateConfig {
    @Autowired
    private lateinit var environment: Environment
    @Bean
    open fun getSessionFactory(): LocalSessionFactoryBean {
        var sessionFactory = LocalSessionFactoryBean()
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(*arrayOf("vn.web.thn.models"))
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory
    }

    @Bean
    open fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(environment.getRequiredProperty("ds.database-driver"))
        dataSource.setUrl(environment.getRequiredProperty("ds.url"))
        dataSource.setUsername(environment.getRequiredProperty("ds.username"))
        dataSource.setPassword(environment.getRequiredProperty("ds.password"))
        return dataSource
    }

    private fun hibernateProperties(): Properties {
        val properties = Properties()
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"))
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"))
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"))
        properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.ddl-auto"))
        properties.put("current_session_context_class", environment.getRequiredProperty("current_session_context_class"))

        return properties
    }
    @Bean
    open fun getTransactionManager(): HibernateTransactionManager {
        var transactionManager = HibernateTransactionManager()
        transactionManager.setSessionFactory(getSessionFactory().getObject())
        return transactionManager
    }
}