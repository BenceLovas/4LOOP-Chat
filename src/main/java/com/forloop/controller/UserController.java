package com.forloop.controller;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.forloop.jpaHandler.EntityGetter;
import com.forloop.model.*;
import com.google.gson.Gson;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
        DirectMessageWindow directMessageWindow = new DirectMessageWindow("chatwiindows", user1, user2);
        DirectMessage directMessage = new DirectMessage(user1, "hello there");
        directMessage.sendMessage(directMessageWindow);
        DirectMessage directMessage2 = new DirectMessage(user1, "geci");
        directMessage2.sendMessage(directMessageWindow);



        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(user1);
        em.persist(user2);
        em.persist(tag);
        em.persist(channelMessage);
        em.persist(reply);
        em.persist(directMessageWindow);
        em.persist(directMessage);
        em.persist(directMessage2);
        em.persist(channel);
        System.out.print("-------------------KURVA ----------------------");
        System.out.println("DM IS + " + directMessageWindow.getDirectMessages().get(1));
        transaction.commit();



    }

    //FOR DEMONSTRATION AND FURTHER TESTING
    private static void populateMEM(){
        User user1 = (User) EntityGetter.getInstance().getEntityById(User.class, 1l);
        User user2 = (User) EntityGetter.getInstance().getEntityById(User.class, 2l);
        Tag tag1 = (Tag) EntityGetter.getInstance().getEntityById(Tag.class, 1l);
        Channel channel1 = (Channel) EntityGetter.getInstance().getEntityById(Channel.class, 1l);
        Reply reply1 = (Reply) EntityGetter.getInstance().getEntityById(Reply.class, 1l);
        ChannelMessage channelMessage1 = (ChannelMessage) EntityGetter.getInstance().getEntityById(ChannelMessage.class, 1l);
        channelMessage1.setMessage("anyad");
        channelMessage1.setAuthor(user2);
        channelMessage1.setChannel(channel1);
        ChannelMessage channelMessage2 = (ChannelMessage) EntityGetter.getInstance().getEntityById(ChannelMessage.class, 1l);
        channelMessage2.setMessage("tied");
        channelMessage1.setChannel(channel1);
        channelMessage2.setAuthor(user1);
        ChannelMessage channelMessage3 = (ChannelMessage) EntityGetter.getInstance().getEntityById(ChannelMessage.class, 1l);
        channelMessage3.setMessage("bazdmegmagad");
        channelMessage3.setAuthor(user1);
        channelMessage1.setChannel(channel1);
        System.out.print("-------------------KURVA ----------------------");
        System.out.println("CHANNEL+MESSAGE IS + " + channelMessage3.getMessage());
        System.out.println("CHANNEL' CHANNELMESSAGE INDEX ' IS = " + channel1.getChannelMessages().get(1));



        System.out.println("---------------------------MEMORY TESTING BEHING HERE---------------------------");
           System.out.println("USER1 EMAIL IS = " + user1.getEmail());
           System.out.println("channel1 userlist is = " + channel1.getUserList().get(0).toString());
           System.out.println("Channel of channelmessage 1 is = " + channelMessage1.getChannel().getName());
           System.out.println("List of channels by User who did reply1 is  = " + reply1.getUser().getChannels().toString());
           System.out.println("List of channels what has a common tag1 is = " + tag1.getChannels().toString());
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

    //MUST BE BIND TO A CHATWINDOW
    @GetMapping(value = "/get-messages", produces = "application/json")
    public List getAllDM() throws JsonProcessingException {

        DirectMessageWindow chatWindows = (DirectMessageWindow) EntityGetter.getInstance().getEntityById(DirectMessageWindow.class, 1);
        List<DirectMessage> directMessages = chatWindows.getDirectMessages();
        return directMessages;



    }
}

