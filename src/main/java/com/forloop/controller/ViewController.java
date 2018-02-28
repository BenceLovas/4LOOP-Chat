package com.forloop.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class ViewController {

    @RequestMapping("/")
    public String root() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(false);
        if (session == null) {
            return "redirect:/login";
        }
        return "redirect:/index";
    }

    @RequestMapping("/index")
    public String index(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        return "index";
    }

    @RequestMapping(value = "/notification-sound")
    public ResponseEntity download(){
        InputStream inputStream;
        String filePath = "static/script/channel.js";
        try {
            inputStream = new FileInputStream(new File(filePath));
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(Files.size(Paths.get(filePath)));
            return new ResponseEntity(inputStreamResource, headers, HttpStatus.OK);
        } catch (IOException e){
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

@RequestMapping("/login")
    public String login() {
        return "login";
    }
}
