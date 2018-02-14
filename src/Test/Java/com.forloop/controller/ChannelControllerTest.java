package Java.com.forloop.controller;

import com.forloop.Exceptions.NameAlreadyTakenException;
import com.forloop.controller.ChannelController;
import com.forloop.controller.UserController;
import com.forloop.model.Channel;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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


}

