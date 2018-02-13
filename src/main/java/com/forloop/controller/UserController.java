package com.forloop.controller;


import com.forloop.model.*;
import com.forloop.persistence.PersistenceManager;
import com.forloop.service.UserService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping(value = "/user/registration", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity registration(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            HttpSession session) {

        User userToCheck = new User(username, password, email);
        User processedUser = service.registration(userToCheck);
        if (processedUser != null) {
            session.setAttribute("userId", processedUser.getId());
            return ResponseEntity.ok(Collections.singletonMap("redirect", "/index"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("response", "Username already in use."));
    }

    @PostMapping(value = "/user/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session) {

        User user = service.login(username, password);
        if (user != null) {
            session.setAttribute("userId", user.getId());
            return ResponseEntity.ok(Collections.singletonMap("redirect", "/index"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("response", "Wrong username or password."));
    }

    @GetMapping(value = "/logout")
    public RedirectView logout(HttpSession session) {
        session.removeAttribute("userId");
        return new RedirectView("/");
    }
}
