package com.forloop.controller;


import com.forloop.model.Channel;
import com.forloop.model.ChannelMessage;
import com.forloop.model.User;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketMessageController {

    //"/socket-storer" prefix automaticly gets here somewhy
    @MessageMapping("/channel/{channelId}")
    //gets the socket from this url
    @SendTo("/socket-listener/channel/{channelId}")
    //Sends it to the users who are subscribed to this url
    public String greeting(@DestinationVariable String channelId, String message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return "the channel id where the signal was sent was" + channelId;
    }

}