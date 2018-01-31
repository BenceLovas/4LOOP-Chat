package com.forloop.controller;


import com.forloop.jpaHandler.EntityGetter;
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

        List<Tag> tagList = new ArrayList<>();List<User> userList = new ArrayList<>();

        tagList.add(tag);userList.add(user1);userList.add(user2);
        Channel channel = new Channel("Games", userList, user1, tagList, new ArrayList<>());
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

        populateMEM();

    }

    //FOR DEMONSTRATION AND FURTHER TESTING
    private static void populateMEM(){
           User user1 = (User) EntityGetter.getInstance().getEntityById(User.class, 1l);
           User user2 = (User) EntityGetter.getInstance().getEntityById(User.class, 2l);
           Tag tag1 = (Tag) EntityGetter.getInstance().getEntityById(Tag.class, 1l);
           Channel channel1 = (Channel) EntityGetter.getInstance().getEntityById(Channel.class, 1l);
           Reply reply1 = (Reply) EntityGetter.getInstance().getEntityById(Reply.class, 1l);
           ChannelMessage channelMessage1 = (ChannelMessage) EntityGetter.getInstance().getEntityById(ChannelMessage.class, 1l);

           System.out.println("---------------------------MEMORY TESTING BEHING HERE---------------------------");
           System.out.println("USER1 EMAIL IS = " + user1.getEmail());
           System.out.println("channel1 userlist is = " + channel1.getUserList().get(0).toString());
           System.out.println("Channel of channelmessage 1 is = " + channelMessage1.getChannel().getName());
           System.out.println("List of channels by User who did reply1 is  = " + reply1.getUser().getChannels().toString());
           System.out.println("List of channels what has a common tag1 is = " + tag1.getChannels().toString());
    }

    @GetMapping(value = "/users", produces = "application/json")
    public List<Tag> getUsers() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("applicationPU");
        EntityManager em = emf.createEntityManager();

        populateDB(em);

        em.getTransaction().begin();
        List<User> users = em.createQuery(
                "SELECT u FROM User u").getResultList();

        List<Tag> channels = em.createNamedQuery("getAllTags")
                .getResultList();
        em.getTransaction().commit();

        em.close();
        emf.close();

        return channels;

    }

}
