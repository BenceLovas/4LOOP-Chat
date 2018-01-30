package com.forloop.controller;


import com.forloop.model.*;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    public UserController() {
    }

    private static void populateDB(EntityManager em) {

        User user1 = new User("Jocó", "pass", "email", new ArrayList<>(), 0);
        User user2 = new User("Karesz", "pass1", "email2", new ArrayList<>(), 1);

        Tag tag = new Tag("New Tag", new ArrayList<>());

        List<Tag> tagList = new ArrayList<>();

        tagList.add(tag);

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);


        Channel channel = new Channel("Games", userList, user1, tagList);

        ChannelMessage channelMessage = new ChannelMessage("New channelmsg", user1, channel);
        Reply reply = new Reply(channelMessage, "replyFromJC", user1);

        channelMessage.getReplies().add(reply);

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(user1);
        em.persist(user2);
        em.persist(tag);
        em.persist(channel);
        em.persist(channelMessage);
        em.persist(reply);
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
