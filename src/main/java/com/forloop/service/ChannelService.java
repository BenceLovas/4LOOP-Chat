package com.forloop.service;

import com.forloop.dao.*;
import com.forloop.exceptions.NameAlreadyTakenException;
import com.forloop.model.Channel;
import com.forloop.model.ChannelMessage;
import com.forloop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import static java.lang.Math.toIntExact;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChannelService {

    private ChannelMessageDAOJPA channelMessageDAOJPA;
    private UserDAOJPA userDAOJPA;
    private ChannelDAOJPA channelDAOJPA;

    public ChannelService(ChannelDAOJPA channelDAOJPA, UserDAOJPA userDAOJPA, ChannelMessageDAOJPA channelMessageDAOJPA) {
        this.channelDAOJPA = channelDAOJPA;
        this.userDAOJPA = userDAOJPA;
        this.channelMessageDAOJPA = channelMessageDAOJPA;
    }

    public Channel addNewChannel(long userId, String channelName) throws NameAlreadyTakenException {


        User author = userDAOJPA.findOne(userId);
        Channel channel = new Channel(channelName, author);
        channel.addUserToChannel(author);
        channelDAOJPA.save(channel);
        return channel;

    }

    public Channel addNewPrivateChannel(Long userId, String channelName, String password) throws NameAlreadyTakenException {
        User author = userDAOJPA.findOne(userId);
        Channel channel = new Channel(channelName, author, password);
        channel.addUserToChannel(author);
        channelDAOJPA.save(channel);
        return channel;
    }

    public List<Channel> getUserChannels(long userId){

        return channelDAOJPA.findByUserListId(userId);

    }

    public List<ChannelMessage> getChannelMessages(long channelId){

        return channelMessageDAOJPA.findAllByChannelId(channelId);
    }

    public void addNewChannelMessage(String message, long userId, long channelId){
        User author = userDAOJPA.findOne(userId);

        Channel channel = channelDAOJPA.findOne(channelId);
        ChannelMessage newMessage = new ChannelMessage(message, author, channel);
        channel.addMessageToChannel(newMessage);

        channelMessageDAOJPA.save(newMessage);
    }

    public Channel addUserToChannel(long userId, long channelId) {
        User user = userDAOJPA.findOne(userId);
        Channel channel = channelDAOJPA.findOne(channelId);
        channel.addUserToChannel(user);
        channelDAOJPA.save(channel);
        return channelDAOJPA.findOne(channelId);
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
        return channelMessageDAOJPA.findTopByChannelIdOrderByIdDesc(channelId);
    }

    public List<Channel> getAllChannels() {

        return channelDAOJPA.findAll();
    }

    public List<Channel> listAllChannelsBy(String by){

        switch (by){
            case "nameASC":
                return channelDAOJPA.findAll(new Sort(Sort.Direction.ASC, "name"));
            case "nameDESC":
                return channelDAOJPA.findAll(new Sort(Sort.Direction.DESC, "name"));

            case "dateASC":
                return channelDAOJPA.findAll(new Sort(Sort.Direction.ASC, "creationDate"));

            case "dateDESC":
                return channelDAOJPA.findAll(new Sort(Sort.Direction.DESC, "creationDate"));

            default:
                return channelDAOJPA.findAll(new Sort(Sort.Direction.ASC, "name"));

        }
    }

    public List<Map<String, Object>> findJoinedChannels(long userId, List<Channel> allChannels){
        List<Channel> userChannels = this.getUserChannels(userId);

        List<Map<String, Object>> jsonChannels = new ArrayList<>();
        for (Channel channel : allChannels) {
            Map<String, Object> currentChannel = new HashMap<>();
            currentChannel.put("channel", channel);
            currentChannel.put("joined", userChannels.contains(channel));
            jsonChannels.add(currentChannel);
        }
        return jsonChannels;

    }

    public Map<String, Object> jsonBuilder(String key, Object object){
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put(key, object);
        return jsonMap;
    }

    public boolean isPasswordValid(Integer channelId, String password) {
        Channel channel = channelDAOJPA.findOne((long) channelId);
        if(channel.getPassword().equals(password)){
            System.out.println("CORRECT PW");
            return true;
        }
        return false;
    }

}
