package com.forloop.controller;

import com.forloop.exceptions.NameAlreadyTakenException;
import com.forloop.model.Channel;
import com.forloop.model.ChannelMessage;
import com.forloop.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

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

    @GetMapping(value = "/get-user-channels", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getChannels(
            HttpSession session){

        Long userId = (long) session.getAttribute("userId");
        List<Channel> userChannels = service.getUserChannels(userId);
        Map<String, Object> JSONMap = new HashMap<String, Object>(){{
            put("channels", userChannels);
        }};
        return ResponseEntity.ok(JSONMap);
    }

    @GetMapping(value = "/get-all-channels", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getAllChannels(HttpSession session) {

        Long userId = (long) session.getAttribute("userId");

        List<Channel> userChannels = service.getUserChannels(userId);
        List<Channel> allChannels = service.getAllChannels();
        List<Map<String, Object>> jsonChannels = new ArrayList<>();

        for (Channel channel : allChannels) {
            Map<String, Object> currentChannel = new HashMap<>();
            currentChannel.put("channel", channel);
            currentChannel.put("joined", userChannels.contains(channel));
            jsonChannels.add(currentChannel);
        }

        Map<String, Object> JSONMap = new HashMap<String, Object>() {{
            put("channels", jsonChannels);
        }};
        return ResponseEntity.ok(JSONMap);
    }

    @GetMapping(value = "/sort-by/{name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity sortBy(
            @PathVariable(value = "name") String name,
            HttpSession session){

        Long userId = (long) session.getAttribute("userId");
        List<Channel> userChannels = service.getUserChannels(userId);
        List<Channel> listedChannels = service.listAllChannelsBy(name);
        List<Map<String, Object>> jsonChannels = new ArrayList<>();

        for (Channel channel : listedChannels) {
            Map<String, Object> currentChannel = new HashMap<>();
            currentChannel.put("channel", channel);
            currentChannel.put("joined", userChannels.contains(channel));
            jsonChannels.add(currentChannel);
        }

        Map<String, Object> JSONMAP = new HashMap<String, Object>(){{
            put("channels", jsonChannels);
        }};
        return ResponseEntity.ok(JSONMAP);
    }


    @GetMapping(value = "/channel/{channelId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity loadChannel(
            @PathVariable(value="channelId") Integer channelId,
            HttpSession session) {

        List<ChannelMessage> channelMessages = service.getChannelMessages(channelId);
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

        long userId = (long) session.getAttribute("userId");
        service.addNewChannelMessage(message, userId, channelId);
        List<ChannelMessage> channelMessages = service.getChannelMessages(channelId);

        Map<String, Object> JSONMAP = new HashMap<String, Object>(){{
            put("channelMessages", channelMessages);
        }};
        return ResponseEntity.ok(JSONMAP);
    }

    @PostMapping(value = "/add-user-to-channel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity addUserToChannel(@RequestParam Integer channelId, HttpSession session) {
        long userId = (long) session.getAttribute("userId");
        List<Channel> updatedChannelList = service.addUserToChannel(userId, (long) channelId);

        return ResponseEntity.ok(updatedChannelList);
    }

    @GetMapping(value = "get-all-user-channel-id", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getAllUserChannelId(HttpSession session){
        long userId = (long) session.getAttribute("userId");
        List<Integer> channelIdList = service.getUserChannelIds(userId);
        Map<String, Object> JSONMAP = new HashMap<>();
        JSONMAP.put("channelIds", channelIdList);
        return ResponseEntity.ok(JSONMAP);
    }

}

