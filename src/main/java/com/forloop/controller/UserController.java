package com.forloop.controller;


import com.forloop.model.*;
import com.forloop.persistence.PersistenceManager;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private static EntityManager entityManager = PersistenceManager.getInstance().getEntityManager();

    public UserController() {
    }

    @PostMapping(value = "/user/registration", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity registration(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            HttpSession session) {


        User user = new User(username, password, email);

        entityManager.getTransaction().begin();
        try {
            entityManager.persist(user);
            entityManager.getTransaction().commit();
        } catch (PersistenceException exception) {
            entityManager.getTransaction().rollback();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("response", "Username already in use"));
        }
        session.setAttribute("userId", user.getId());
        return ResponseEntity.ok(Collections.singletonMap("redirect", "/index"));
    }

    @PostMapping(value = "/user/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session) {

        try {
            User user = (User) entityManager.createNamedQuery("findUserByName").setParameter("name", username).getSingleResult();
            if (!password.equals(user.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("response", "Wrong password"));
            }
            session.setAttribute("userId", user.getId());
            return ResponseEntity.ok(Collections.singletonMap("redirect", "/index"));
        } catch (NoResultException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("response", "Username doesn't exist"));
        }

    }

    @GetMapping(value = "/logout")
    public RedirectView logout(
            HttpSession session) {
        session.removeAttribute("userId");
        return new RedirectView("/");
    }
    
    private static void populateDB() {
        User user1 = new User("Jocó", "pass", "email");
        User user2 = new User("Karesz", "pass1", "email2");
        User user3 = new User("Feri", "pass1", "email3");
        User user4 = new User("Bela", "pass1", "email4");
        User user5 = new User("Juci", "pass1", "email5");

        Tag tag1 = new Tag("Tag1", new ArrayList<>());
        Tag tag2 = new Tag("Tag2", new ArrayList<>());
        Tag tag3 = new Tag("Tag3", new ArrayList<>());
        Tag tag4 = new Tag("Tag4", new ArrayList<>());
        Tag tag5 = new Tag("Tag5", new ArrayList<>());

        List<Tag> tagList = new ArrayList<>();

        tagList.add(tag1);
        tagList.add(tag2);
        tagList.add(tag3);
        tagList.add(tag4);
        tagList.add(tag5);

        List<User> userList = new ArrayList<>();
        List<User> userList2 = new ArrayList<>();

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        userList2.add(user2);
        userList2.add(user3);
        userList2.add(user4);
        userList2.add(user5);

        Channel channel1 = new Channel("Games", user1);
        ChannelMessage channelMessage = new ChannelMessage("New channelmsg", user1, channel1);

        Channel channel2 = new Channel("Politics", user2);

        Reply reply = new Reply(channelMessage, "replyFromJC", user1);
        channelMessage.getReplies().add(reply);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.persist(user4);
        entityManager.persist(user5);
        entityManager.persist(tag1);
        entityManager.persist(tag2);
        entityManager.persist(tag3);
        entityManager.persist(tag4);
        entityManager.persist(tag5);
        entityManager.persist(channel1);
        entityManager.persist(channel2);
        entityManager.persist(channelMessage);
        entityManager.persist(reply);
        transaction.commit();

    }


}
