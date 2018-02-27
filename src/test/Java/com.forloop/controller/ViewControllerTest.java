package Java.com.forloop.controller;

import com.forloop.controller.ViewController;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import javax.xml.ws.spi.http.HttpExchange;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ViewControllerTest {

    @Mock
    private HttpSession session;

    @Mock
    private ServletRequestAttributes attr;

    ViewController viewController;

    @BeforeEach
    public void setUp(){
        session = mock(HttpSession.class);

    }


/*
    @Test
    public void rootNullTest() {
        ViewController viewController = new ViewController();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(attr.getRequest().getSession(false)).thenReturn(null);
        Assertions.assertEquals(viewController.root(), "redirect:/login");
    }

    @Test
    public void rootNotnullTest(){
        ViewController viewController = new ViewController();
        when(session.getAttribute("userId")).thenReturn(1);
        Assertions.assertEquals(viewController.root(), "redirect:/index");
    }
*/

    @Test
    public void IndexNullTest() {
        ViewController viewController = new ViewController();
        when(session.getAttribute("userId")).thenReturn(null);
        Assertions.assertEquals(viewController.index(session), "redirect:/login");
    }

    @Test
    public void IndexNotnull(){
        ViewController viewController = new ViewController();
        when(session.getAttribute("userId")).thenReturn(1);
        Assertions.assertEquals(viewController.index(session), "index");
    }



}
