package com.forloop.service;

import com.forloop.dao.UserDAO;
import com.forloop.dao.UserDAOJPA;
import com.forloop.exceptions.NameAlreadyTakenException;
import com.forloop.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserDAOJPA userDAOJPA;

    public UserService(UserDAOJPA userDAOJPA) {
        this.userDAOJPA = userDAOJPA;
    }

    public User registration(User user) throws NameAlreadyTakenException {
        userDAOJPA.save(user);
        return user;
        //return dao.insertUser(user);
    }

    public User login(String username, String password) {

        User userInDB = userDAOJPA.getByName(username);
        if (userInDB != null && password.equals(userInDB.getPassword())) {
            return userInDB;
        }
        return null;
    }
}
