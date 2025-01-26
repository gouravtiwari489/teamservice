package com.matawan.teamservice.configuration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityManagerConfiguration {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public EntityManager entityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
