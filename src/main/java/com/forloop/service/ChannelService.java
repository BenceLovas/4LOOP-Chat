package com.forloop.service;

import com.forloop.Exceptions.NameAlreadyTakenException;
import com.forloop.dao.ChannelDAOHibernate;
import com.forloop.model.Channel;
import com.forloop.model.ChannelMessage;
import com.forloop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {

    ChannelDAOHibernate channelDAOHibernate;

    @Autowired
    public ChannelService(ChannelDAOHibernate channelDAOHibernate) {
        this.channelDAOHibernate = channelDAOHibernate;
    }

    public Channel addNewChannel(long userId, String channelName) throws NameAlreadyTakenException {

        User author = channelDAOHibernate.findUserById(userId);
        Channel channel = new Channel(channelName, author);
        channel.addUserToChannel(author);
        channelDAOHibernate.insertChannel(channel);
        return channel;

    }

    public List<Channel> getUserChannels(long userId){

        return channelDAOHibernate.findUserChannels(userId);
    }

    public List<ChannelMessage> getChannelMessages(long channelId){

        return channelDAOHibernate.getChannelMessages(channelId);
    }

    public void addNewChannelMessage(String message, long userId, long channelId){
        User author = channelDAOHibernate.findUserById(userId);

        Channel channel = channelDAOHibernate.findChannel(channelId);
        ChannelMessage newMessage = new ChannelMessage(message, author, channel);
        channel.addMessageToChannel(newMessage);

        channelDAOHibernate.addNewChannelMessage(channel, newMessage);
    }

    public List<Channel> addUserToChannel(long userId, long channelId) {
        User user = channelDAOHibernate.findUserById(userId);
        Channel channel = channelDAOHibernate.findChannel(channelId);
        channel.addUserToChannel(user);
        channelDAOHibernate.updateChannel(channel);
        return channelDAOHibernate.findUserChannels(userId);
    }
}
