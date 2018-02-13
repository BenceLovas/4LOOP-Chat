package com.forloop.dao;

import com.forloop.Exceptions.NameAlreadyTakenException;
import com.forloop.model.Channel;
import com.forloop.model.ChannelMessage;
import com.forloop.model.User;
import com.forloop.persistence.PersistenceManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.Collections;
import java.util.List;

@Repository
public class ChannelDAOHibernate {

    private static EntityManager entityManager = PersistenceManager.getInstance().getEntityManager();

    public void insertChannel(Channel channel) throws NameAlreadyTakenException{

        entityManager.getTransaction().begin();
        try {
            entityManager.persist(channel);
            entityManager.getTransaction().commit();
        } catch (PersistenceException exception) {
            entityManager.getTransaction().rollback();
            throw new NameAlreadyTakenException("Channel already exists. Please find another name.");
        }
    }

    public void updateChannel(Channel channel) {
        entityManager.getTransaction().begin();
        entityManager.persist(channel);
        entityManager.getTransaction().commit();
    }

    public User findUserById(long userId){
        return entityManager.find(User.class, userId);
    }

    public List<Channel> findUserChannels(long userId){

        List<Channel> userChannels = entityManager.createNamedQuery("getChannelsByUserId")
                .setParameter("userId", userId)
                .getResultList();

        return userChannels;
    }

    public List<ChannelMessage> getChannelMessages(long channelId){
        List<ChannelMessage> channelMessages = (List<ChannelMessage> )entityManager.createNamedQuery("getAllChannelMessagesByChannelId").setParameter("channelId", Long.valueOf(channelId)).getResultList();
        return channelMessages;
    }

    public void addNewChannelMessage(Channel channel, ChannelMessage newMessage){

        entityManager.getTransaction().begin();
        entityManager.persist(channel);
        entityManager.persist(newMessage);
        entityManager.getTransaction().commit();

    }

    public Channel findChannel(long channelId){
        return entityManager.find(Channel.class, channelId);
    }

}
