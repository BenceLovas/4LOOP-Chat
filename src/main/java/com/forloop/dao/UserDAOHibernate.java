package com.forloop.dao;

import com.forloop.exceptions.NameAlreadyTakenException;
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
public class UserDAOHibernate implements UserDAO {

    private PersistenceManager persistenceManager = new PersistenceManager();
    private EntityManager entityManager = persistenceManager.getEntityManager();

    public UserDAOHibernate() {}

    @Override
    public User insertUser(User user) throws NameAlreadyTakenException {
        if(!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
            try {
                entityManager.persist(user);
                entityManager.getTransaction().commit();
                return user;
            } catch (PersistenceException exception) {
                entityManager.getTransaction().rollback();
                throw new NameAlreadyTakenException("Username already in use.");
            }
        }
        return null;
    }

    @Override
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
