package Java.com.forloop.controller;

import com.forloop.Exceptions.NameAlreadyTakenException;
import com.forloop.controller.ChannelController;
import com.forloop.controller.UserController;
import com.forloop.model.Channel;
import com.forloop.model.ChannelMessage;
import com.forloop.model.User;
import com.forloop.service.ChannelService;
import com.forloop.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ChannelControllerTest {

    @Mock
    private ChannelService service;

    @Mock
    private HttpSession session;

    private ChannelController channelController;

    @Test
    public void newChannelErrorTest() throws NameAlreadyTakenException {
            service = mock(ChannelService.class);
            session = mock(HttpSession.class);
            when(service.addNewChannel(any(Long.class), any(String.class))).thenThrow(new NameAlreadyTakenException("name already taken"));
            when(session.getAttribute(any(String.class))).thenReturn(3l);

            channelController = new ChannelController(service);

            Assertions.assertEquals(channelController.newChannel("channelName", session),
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("response", "name already taken")
            ));
    }

    @Test
    public void newChannelTest() {
        service = mock(ChannelService.class);
        session = mock(HttpSession.class);
        Channel testChannel = new Channel();
        ArrayList testList = new ArrayList<>();
        testList.add(testChannel);
        try {
            when(service.addNewChannel(any(Long.class), any(String.class))).thenReturn(testChannel);
        } catch (NameAlreadyTakenException e) {
            e.printStackTrace();
        }
        when(session.getAttribute(any(String.class))).thenReturn(3l);
        when(service.getUserChannels(any(long.class))).thenReturn(testList);

        channelController = new ChannelController(service);

        Assertions.assertEquals(channelController.newChannel("channelname", session),
                ResponseEntity.ok(
                new HashMap<String, Object>(){{
                    put("newChannel", testChannel);
                    put("channels",testList );
                }}));

    }

    @Test
    public void getChannelTest(){
        service = mock(ChannelService.class);
        session = mock(HttpSession.class);

        ArrayList<Channel> testChannelList = new ArrayList<>();
        Channel testChannel = new Channel();
        testChannelList.add(testChannel);
        Map<String, Object> testJson = new HashMap<>();
        testJson.put("channels", testChannelList);
        channelController = new ChannelController(service);

        when(session.getAttribute(any(String.class))).thenReturn(1l);
        when(service.getUserChannels(any(long.class))).thenReturn(testChannelList);
        Assertions.assertEquals(channelController.getChannels(session), ResponseEntity.ok(testJson));
    }

    @Test
    public void getAllChannelsTest(){
        service = mock(ChannelService.class);
        session = mock(HttpSession.class);

        ArrayList<Channel> testChannelList = new ArrayList<>();
        Channel testChannel = new Channel();
        testChannelList.add(testChannel);
        Map<String, Object> testJson = new HashMap<>();
        testJson.put("channels", testChannelList);
        channelController = new ChannelController(service);

        when(session.getAttribute(any(String.class))).thenReturn(1l);
        when(service.getAllChannels()).thenReturn(testChannelList);
        Assertions.assertEquals(channelController.getAllChannels(session), ResponseEntity.ok(testJson));
    }

    @Test
    public void loadChannelTest(){
        service = mock(ChannelService.class);
        session = mock(HttpSession.class);

        List<ChannelMessage> channelMessageListTest = new ArrayList<>();
        ChannelMessage channelMessageTest = new ChannelMessage();
        channelMessageListTest.add(channelMessageTest);
        Map<String, Object> testJson = new HashMap<>();
        testJson.put("channelMessages", channelMessageListTest);

        when(service.getChannelMessages(any(long.class))).thenReturn(channelMessageListTest);

        channelController = new ChannelController(service);

        Assertions.assertEquals(channelController.loadChannel(1, session),
                ResponseEntity.ok(testJson));

    }

    @Test
    public void addMessageTest(){
        service = mock(ChannelService.class);
        session = mock(HttpSession.class);


        List<ChannelMessage> channelMessageListTest = new ArrayList<>();
        ChannelMessage channelMessageTest = new ChannelMessage();
        channelMessageListTest.add(channelMessageTest);
        Map<String, Object> testJson = new HashMap<>();
        testJson.put("channelMessages", channelMessageListTest);


        when(session.getAttribute(any(String.class))).thenReturn(3l);
        doNothing().when(service).addNewChannelMessage(any(String.class), any(long.class), any(long.class));
        when(service.getChannelMessages(any(long.class))).thenReturn(channelMessageListTest);


        channelController = new ChannelController(service);

        Assertions.assertEquals(channelController.addMessage("a", 2, session),
                ResponseEntity.ok(testJson));

    }

/*
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
 */


}

