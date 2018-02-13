package com.forloop.dao;

import com.forloop.Exceptions.NameAlreadyTakenException;
import com.forloop.model.Channel;
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

    public void persistChannel(Channel channel) throws NameAlreadyTakenException{

        entityManager.getTransaction().begin();
        try {
            entityManager.persist(channel);
            entityManager.getTransaction().commit();
        } catch (PersistenceException exception) {
            entityManager.getTransaction().rollback();
            throw new NameAlreadyTakenException("Channel already exists. Please find another name.");
        }
    }

    public User findAuthor(long userId){
        return entityManager.find(User.class, userId);
    }

    public List<Channel> findUserChannels(long userId){

        List<Channel> userChannels = entityManager.createNamedQuery("getChannelsByUserId")
                .setParameter("userId", userId)
                .getResultList();

        return userChannels;
    }

}
