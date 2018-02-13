package com.forloop.service;

import com.forloop.Exceptions.NameAlreadyTakenException;
import com.forloop.dao.ChannelDAOHibernate;
import com.forloop.model.Channel;
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

        User author = channelDAOHibernate.findAuthor(userId);
        Channel channel = new Channel(channelName, author);
        channel.addUserToChannel(author);
        channelDAOHibernate.persistChannel(channel);
        return channel;

    }

    public List<Channel> getUserChannels(long userId){

        return channelDAOHibernate.findUserChannels(userId);
    }
}
