package com.forloop.dao;

import com.forloop.exceptions.NameAlreadyTakenException;
import com.forloop.model.Channel;
import com.forloop.model.ChannelMessage;
import com.forloop.model.User;

import java.util.List;

public interface ChannelDAO {
    void insertChannel(Channel channel) throws NameAlreadyTakenException;

    List<Channel> getAllChannels();

    void updateChannel(Channel channel);

    User findUserById(long userId);

    List<Channel> findUserChannels(long userId);

    List<ChannelMessage> getChannelMessages(long channelId);

    void addNewChannelMessage(Channel channel, ChannelMessage newMessage);

    ChannelMessage getLastChannelMessage(long channelId);

    Channel findChannel(long channelId);

    List<Channel> sortAllChannelsByNameASC();

    List<Channel> sortAllChannelsByNameDESC();

    List<Channel> sortAllChannelByDateASC();

    List<Channel> sortAllChannelByDateDESC();
}
