package com.forloop.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceManager {

    private EntityManagerFactory entityManagerFactory;

    public PersistenceManager() {
        entityManagerFactory = Persistence.createEntityManagerFactory("applicationPU");
    }

    public EntityManager getEntityManager(){
        return entityManagerFactory.createEntityManager();
    }


}
