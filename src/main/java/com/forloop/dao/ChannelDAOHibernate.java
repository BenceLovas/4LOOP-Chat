package com.forloop.dao;

import com.forloop.exceptions.NameAlreadyTakenException;
import com.forloop.model.Channel;
import com.forloop.model.ChannelMessage;
import com.forloop.model.User;
import com.forloop.persistence.PersistenceManager;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
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

    public List<Channel> getAllChannels(){
        return (List<Channel>) entityManager.createNamedQuery("getAllChannels").getResultList();
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

    public ChannelMessage getLastChannelMessage(long channelId){
        Channel channel = entityManager.find(Channel.class, channelId);
        return channel.getChannelMessages().get(channel.getChannelMessages().size() - 1);
    }

    public Channel findChannel(long channelId){
        return entityManager.find(Channel.class, channelId);
    }

    public List<Channel> sortAllChannelsByNameASC(){
        return entityManager.createNamedQuery("getAllChannelsAscByName").getResultList();
    }

    public List<Channel> sortAllChannelsByNameDESC(){
        return entityManager.createNamedQuery("getAllChannelsDescByName").getResultList();
    }

    public List<Channel> sortAllChannelByDateASC(){
        return entityManager.createNamedQuery("getAllChannelsAscByCreationDate").getResultList();
    }

    public List<Channel> sortAllChannelByDateDESC(){
        return entityManager.createNamedQuery("getAllChannelsDescByCreationDate").getResultList();
    }


}
