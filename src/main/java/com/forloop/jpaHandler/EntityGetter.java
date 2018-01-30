package com.forloop.jpaHandler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityGetter {

    private static EntityGetter instance;
    private EntityManagerFactory emf;
    private EntityManager em;


    public static EntityGetter getInstance(){
    if(instance == null){
        instance = new EntityGetter();
    }
    return instance;
    }


    //GetAnyEntityBy a class and a id
    public Object getEntityById(Class classname, long id){
        emf = Persistence.createEntityManagerFactory("EntityGetter");
        em = emf.createEntityManager();
        Object entity = em.find(classname, id);
        em.close();
        emf.close();
        return classname.cast(entity);
    }

}
