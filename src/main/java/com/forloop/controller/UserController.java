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
        User user3 = new User("Feri", "pass1", "email3", new ArrayList<>(), 1);
        User user4 = new User("Bela", "pass1", "email4", new ArrayList<>(), 1);
        User user5 = new User("Juci", "pass1", "email5", new ArrayList<>(), 1);

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

        Channel channel1 = new Channel("Games", userList, user1, tagList, new ArrayList<>());
        ChannelMessage channelMessage = new ChannelMessage("New channelmsg", user1, channel1);

        Channel channel2 = new Channel("Politics", userList2, user2, tagList, new ArrayList<>());

        Reply reply = new Reply(channelMessage, "replyFromJC", user1);
        channelMessage.getReplies().add(reply);

        UserRelation userRelation = new UserRelation(user1, 2);
        UserRelation userRelation2 = new UserRelation(user2, 3);
        userRelation2.setRelationState(RelationState.ACCEPTED);

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);
        em.persist(user4);
        em.persist(user5);
        em.persist(tag1);
        em.persist(tag2);
        em.persist(tag3);
        em.persist(tag4);
        em.persist(tag5);
        em.persist(channel1);
        em.persist(channel2);
        em.persist(channelMessage);
        em.persist(reply);
        em.persist(userRelation);
        em.persist(userRelation2);
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
    public List<User> getUsers() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("applicationPU");
        EntityManager em = emf.createEntityManager();

        populateDB(em);

        em.getTransaction().begin();
        List<User> users = em.createQuery(
                "SELECT u FROM User u").getResultList();
        em.getTransaction().commit();

        List<User> usersByChannel = em.createNamedQuery("findUsersByChannel").setParameter("user_id", 1l).getResultList();

        List<User> userByName = em.createNamedQuery("findUserByName").setParameter("name", "Feri").getResultList();

        List<User> getFriends = em.createNamedQuery("getFriends").setParameter("user_id", 2l).getResultList();

        System.out.println("Friend is" + getFriends.get(0).getName());

        System.out.println(userByName.get(0).getEmail());

        for (User user : usersByChannel){
            System.out.println(user.getName());
        }

        em.close();
        emf.close();

        return users;

    }

}
