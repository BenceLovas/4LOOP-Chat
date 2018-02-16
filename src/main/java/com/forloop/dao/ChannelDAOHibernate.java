package com.forloop.dao;

import com.forloop.exceptions.NameAlreadyTakenException;
import com.forloop.model.Channel;
import com.forloop.model.ChannelMessage;
import com.forloop.model.User;
import com.forloop.persistence.PersistenceManager;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ChannelDAOHibernate implements ChannelDAO {

    private PersistenceManager persistenceManager = new PersistenceManager();
    private EntityManager entityManager = persistenceManager.getEntityManager();

    @Override
    public void insertChannel(Channel channel) throws NameAlreadyTakenException{
        if(!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
            try {
                entityManager.persist(channel);
                entityManager.getTransaction().commit();
            } catch (PersistenceException exception) {
                entityManager.getTransaction().rollback();
                throw new NameAlreadyTakenException("Channel already exists. Please find another name.");
            }
        }
    }

    @Override
    public List<Channel> getAllChannels(){
        return (List<Channel>) entityManager.createNamedQuery("getAllChannels").getResultList();
    }

    @Override
    public void updateChannel(Channel channel) {
        if(!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
            entityManager.persist(channel);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public User findUserById(long userId){
        return entityManager.find(User.class, userId);
    }

    @Override
    public List<Channel> findUserChannels(long userId){

        List<Channel> userChannels = entityManager.createNamedQuery("getChannelsByUserId")
                .setParameter("userId", userId)
                .getResultList();

        return userChannels;
    }

    @Override
    public List<ChannelMessage> getChannelMessages(long channelId){
        List<ChannelMessage> channelMessages = (List<ChannelMessage> )entityManager.createNamedQuery("getAllChannelMessagesByChannelId").setParameter("channelId", Long.valueOf(channelId)).getResultList();
        return channelMessages;
    }

    @Override
    public void addNewChannelMessage(Channel channel, ChannelMessage newMessage){
        if(!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
            entityManager.persist(channel);
            entityManager.persist(newMessage);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public ChannelMessage getLastChannelMessage(long channelId){
        Channel channel = entityManager.find(Channel.class, channelId);
        return channel.getChannelMessages().get(channel.getChannelMessages().size() - 1);
    }

    @Override
    public Channel findChannel(long channelId){
        return entityManager.find(Channel.class, channelId);
    }

    @Override
    public List<Channel> sortAllChannelsByNameASC(){
        return entityManager.createNamedQuery("getAllChannelsAscByName").getResultList();
    }

    @Override
    public List<Channel> sortAllChannelsByNameDESC(){
        return entityManager.createNamedQuery("getAllChannelsDescByName").getResultList();
    }

    @Override
    public List<Channel> sortAllChannelByDateASC(){
        return entityManager.createNamedQuery("getAllChannelsAscByCreationDate").getResultList();
    }

    @Override
    public List<Channel> sortAllChannelByDateDESC(){
        return entityManager.createNamedQuery("getAllChannelsDescByCreationDate").getResultList();
    }


}
