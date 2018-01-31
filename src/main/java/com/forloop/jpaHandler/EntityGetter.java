package com.forloop.jpaHandler;

import com.forloop.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

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

    private EntityGetter(){

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
