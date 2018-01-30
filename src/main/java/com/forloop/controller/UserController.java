package com.forloop.controller;


import com.forloop.model.User;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

@RestController
public class UserController {

    public UserController() {
    }

    private static void populateDB(EntityManager em) {

        User user1 = new User("Jocó");
        User user2 = new User("Karesz");

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(user1);
        em.persist(user2);
        transaction.commit();

    }

    @GetMapping(value = "/users", produces = "application/json")
    public List<User> getUsers() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("applicationPU");
        EntityManager em = emf.createEntityManager();

        populateDB(em);

        em.getTransaction().begin();
        List<User> users = em.createQuery(
                "SELECT u FROM User u").getResultList();
        em.getTransaction().commit();

        em.close();
        emf.close();
        return users;
    }

}
