package com.readingrover.springbootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;


@SpringBootApplication
@EntityScan("com.readingrover.springbootdemo.data.model")
@EnableJpaRepositories("com.readingrover.springbootdemo.data.repos")
public class Application
{
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter()
    {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        return loggingFilter;
    }
    
    @Bean
    public JpaVendorAdapter jpaVendorAdapter()
    {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setShowSql(true);
        
        return jpaVendorAdapter;
    }
}
