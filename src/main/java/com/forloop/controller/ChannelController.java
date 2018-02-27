package com.forloop.controller;

import com.forloop.exceptions.NameAlreadyTakenException;
import com.forloop.model.Channel;
import com.forloop.model.ChannelMessage;
import com.forloop.service.ChannelService;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
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

        return ResponseEntity.ok(service.jsonBuilder("channels", userChannels));
    }

    @GetMapping(value = "/get-all-channels", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getAllChannels(HttpSession session) {

        Long userId = (long) session.getAttribute("userId");
        List<Channel> allChannels = service.getAllChannels();
        Map<String, Object> JSONMap = new HashMap<String, Object>() {{
            put("channels", service.findJoinedChannels(userId, allChannels));
        }};
        return ResponseEntity.ok(JSONMap);
    }


    @GetMapping(value = "/sort-by/{name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity sortBy(
            @PathVariable(value = "name") String name,
            HttpSession session){

        Long userId = (long) session.getAttribute("userId");
        List<Channel> sortedChannels = service.listAllChannelsBy(name);
        Map<String, Object> JSONMAP = new HashMap<String, Object>(){{
            put("channels", service.findJoinedChannels(userId, sortedChannels));
        }};

        return ResponseEntity.ok(JSONMAP);
    }


    @GetMapping(value = "/channel/{channelId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity loadChannel(
            @PathVariable(value="channelId") Long channelId,
            HttpSession session) {

        List<ChannelMessage> channelMessages = service.getChannelMessages(channelId);

        return ResponseEntity.ok(service.jsonBuilder("channelMessages", channelMessages));
    }

    @PostMapping(value = "/channel/{channelId}/newmessage", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity addMessage(
            @RequestParam String message,
            @RequestParam Integer channelId,
            HttpSession session) {

        long userId = (long) session.getAttribute("userId");
        service.addNewChannelMessage(message, userId, channelId);
        List<ChannelMessage> channelMessages = service.getChannelMessages(channelId);

        return ResponseEntity.ok(service.jsonBuilder("channelMessages", channelMessages));
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

        return ResponseEntity.ok(service.jsonBuilder("channelIds", channelIdList));
    }

    @GetMapping(value = "/emoticon/{emoticonName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getEmoticon(@PathVariable(value="emoticonName") String emoticonName){
        InputStream in = getClass()
                .getResourceAsStream("/static/emoticons/" + emoticonName +".gif");
        try {
            return IOUtils.toByteArray(in);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @GetMapping(value = "/get-channels-by-name/{name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getTop5ChannelsByName(@PathVariable(value="name") String searchTerm){
        List<Channel> channels = service.findTop5ChannelsByName(searchTerm);

        return ResponseEntity.ok(service.jsonBuilder("channels", channels));
    }

}

