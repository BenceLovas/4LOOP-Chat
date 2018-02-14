package com.forloop.service;

import com.forloop.exceptions.NameAlreadyTakenException;
import com.forloop.dao.ChannelDAOHibernate;
import com.forloop.model.Channel;
import com.forloop.model.ChannelMessage;
import com.forloop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static java.lang.Math.toIntExact;


import java.util.ArrayList;
import java.util.List;

@Service
public class ChannelService {

    ChannelDAOHibernate dao;

    @Autowired
    public ChannelService(ChannelDAOHibernate dao) {
        this.dao = dao;
    }

    public Channel addNewChannel(long userId, String channelName) throws NameAlreadyTakenException {

        User author = dao.findUserById(userId);
        Channel channel = new Channel(channelName, author);
        channel.addUserToChannel(author);
        dao.insertChannel(channel);
        return channel;

    }

    public List<Channel> getUserChannels(long userId){

        return dao.findUserChannels(userId);
    }

    public List<ChannelMessage> getChannelMessages(long channelId){

        return dao.getChannelMessages(channelId);
    }

    public void addNewChannelMessage(String message, long userId, long channelId){
        User author = dao.findUserById(userId);

        Channel channel = dao.findChannel(channelId);
        ChannelMessage newMessage = new ChannelMessage(message, author, channel);
        channel.addMessageToChannel(newMessage);

        dao.addNewChannelMessage(channel, newMessage);
    }

    public List<Channel> addUserToChannel(long userId, long channelId) {
        User user = dao.findUserById(userId);
        Channel channel = dao.findChannel(channelId);
        channel.addUserToChannel(user);
        dao.updateChannel(channel);
        return dao.findUserChannels(userId);
    }

    public List<Integer> getUserChannelIds(long userId){
        List<Channel> channels = this.getUserChannels(userId);
        List<Integer> userChannelIds = new ArrayList<>();
        for (Channel channel:channels) {
            userChannelIds.add(toIntExact(channel.getId()));
        }
        return userChannelIds;
    }

    public ChannelMessage getLastChannelMessage(long channelId){
        return dao.getLastChannelMessage(channelId);
    }

    public List<Channel> getAllChannels() {
        return dao.getAllChannels();
    }
}
