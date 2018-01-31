package com.forloop.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceManager {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;


    private PersistenceManager() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("applicationPU");
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    private static class LazyHolder {
        static final PersistenceManager INSTANCE = new PersistenceManager();
    }

    public static PersistenceManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
