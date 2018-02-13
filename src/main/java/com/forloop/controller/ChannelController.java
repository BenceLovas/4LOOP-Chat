package com.forloop.controller;

import com.forloop.Exceptions.NameAlreadyTakenException;
import com.forloop.model.Channel;
import com.forloop.model.ChannelMessage;
import com.forloop.model.User;
import com.forloop.persistence.PersistenceManager;
import com.forloop.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpSession;
import javax.xml.ws.spi.http.HttpExchange;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ChannelController {

    private ChannelService service;

    @Autowired
    public ChannelController(ChannelService service) {
        this.service = service;
    }

    @PostMapping(value = "/newchannel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity newChannel(
            @RequestParam String channelName,
            HttpSession session) {

        Long userId = (long) session.getAttribute("userId");
        Channel newChannel;

        try {
            newChannel = service.addNewChannel(userId, channelName);
        } catch (NameAlreadyTakenException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("response", e.getMessage()));
        }
        List<Channel> userChannels = service.getUserChannels(userId);

        Map<String, Object> JSONMap = new HashMap<String, Object>(){{
            put("newChannel", newChannel);
            put("channels", userChannels);
        }};
        return ResponseEntity.ok(JSONMap);
    }

    /*@GetMapping(value = "/getchannels", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getChannels(
            HttpSession session){
        Long userId = (long) session.getAttribute("userId");
        List<Channel> userChannels = entityManager.createNamedQuery("getChannelsByUserId")
                .setParameter("userId", userId)
                .getResultList();
        Map<String, Object> JSONMap = new HashMap<String, Object>(){{
            put("channels", userChannels);
        }};
        return ResponseEntity.ok(JSONMap);

    }

    @GetMapping(value = "/channel/{channelId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity loadChannel(
            @PathVariable(value="channelId") Integer channelId,
            HttpSession session) {
                List<ChannelMessage> channelMessages = (List<ChannelMessage> )entityManager.createNamedQuery("getAllChannelMessagesByChannelId").setParameter("channelId", Long.valueOf(channelId)).getResultList();

        Map<String, Object> JSONMAP = new HashMap<String, Object>(){{
            put("channelMessages", channelMessages);
        }};
        return ResponseEntity.ok(JSONMAP);
    }

    @PostMapping(value = "/channel/{channelId}/newmessage", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity addMessage(
            @RequestParam String message,
            @RequestParam Integer channelId,
            HttpSession session) {

        System.out.println(message);
        System.out.println(channelId);
        User user = entityManager.find(User.class, session.getAttribute("userId"));
        Channel channel = entityManager.find(Channel.class, (long) channelId);
        ChannelMessage newMessage = new ChannelMessage(message, user, channel);
        channel.addMessageToChannel(newMessage);

        entityManager.getTransaction().begin();
        entityManager.persist(channel);
        entityManager.persist(newMessage);
        entityManager.getTransaction().commit();

        List<ChannelMessage> channelMessages = entityManager.createNamedQuery("getAllChannelMessagesByChannelId").setParameter("channelId", (long) channelId).getResultList();


        Map<String, Object> JSONMAP = new HashMap<String, Object>(){{
            put("channelMessages", channelMessages);
        }};
        return ResponseEntity.ok(JSONMAP);

    }*/


}

