package Java.com.forloop.controller;

import com.forloop.controller.ViewController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.servlet.http.HttpSession;
import javax.xml.ws.spi.http.HttpExchange;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ViewControllerTest {

    @Mock
    private HttpSession session;

    ViewController viewController;

    @Test
    public void rootNullTest() {
        session = mock(HttpSession.class);
        ViewController viewController = new ViewController();
        when(session.getAttribute("userId")).thenReturn(null);
        Assertions.assertEquals(viewController.root(session), "redirect:/login");
    }

    @Test
    public void rootNotnullTest(){
        session = mock(HttpSession.class);
        ViewController viewController = new ViewController();
        when(session.getAttribute("userId")).thenReturn(1);
        Assertions.assertEquals(viewController.root(session), "redirect:/index");
    }

    @Test
    public void IndexNullTest() {
        session = mock(HttpSession.class);
        ViewController viewController = new ViewController();
        when(session.getAttribute("userId")).thenReturn(null);
        Assertions.assertEquals(viewController.index(session), "redirect:/login");
    }

    @Test
    public void IndexNotnull(){
        session = mock(HttpSession.class);
        ViewController viewController = new ViewController();
        when(session.getAttribute("userId")).thenReturn(1);
        Assertions.assertEquals(viewController.index(session), "index");
    }



}
