package com.forloop.service;

import com.forloop.dao.UserDaoHibernate;
import com.forloop.exceptions.NameAlreadyTakenException;
import com.forloop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserDaoHibernate dao;

    @Autowired
    public UserService(UserDaoHibernate dao) {
        this.dao = dao;
    }

    public User registration(User user) throws NameAlreadyTakenException {
        return dao.insertUser(user);
    }

    public User login(String username, String password) {
        User userInDB = dao.getUserByName(username);
        if (userInDB != null && password.equals(userInDB.getPassword())) {
            return userInDB;
        }
        return null;
    }
}
