package com.forloop.controller;


import com.forloop.model.Channel;
import com.forloop.model.ChannelMessage;
import com.forloop.model.User;
import com.forloop.service.ChannelService;
import com.forloop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SocketMessageController {

    private ChannelService service;

    @Autowired
    public SocketMessageController(ChannelService service) {
        this.service = service;
    }

    //"/socket-storer" prefix automaticly gets here somewhy
    @MessageMapping("/channel/{channelId}")
    //gets the socket from this url
    @SendTo("/socket-listener/channel/{channelId}")
    //Sends it to the users who are subscribed to this url


    public ResponseEntity reactToSignal(@DestinationVariable String channelId) throws Exception {
        ChannelMessage channelMessage = service.getLastChannelMessage(Long.valueOf(channelId));
        Map<String, Object> JSONMap = new HashMap<String, Object>() {{
            put("channelMessage", channelMessage);
        }};
        return ResponseEntity.ok(JSONMap);
    }

}