package com.forloop.dao;

import com.forloop.model.*;
import com.forloop.persistence.PersistenceManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class UserDaoHibernate {

    private static EntityManager entityManager = PersistenceManager.getInstance().getEntityManager();

    public UserDaoHibernate() {}

    public User insertUser(User user) {
        entityManager.getTransaction().begin();
        try {
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            return user;
        } catch (PersistenceException exception) {
            entityManager.getTransaction().rollback();
            return null;
        }
    }

    public User getUserByName(String username) {
        try {
            return (User) entityManager.createNamedQuery("findUserByName")
                                       .setParameter("name", username)
                                       .getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }
}
