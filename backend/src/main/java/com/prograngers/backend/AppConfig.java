package com.prograngers.backend;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final EntityManager em;

    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(em);
    }

}
