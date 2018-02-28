package com.forloop.service;

import com.forloop.dao.UserDAO;
import com.forloop.dao.UserDAOJPA;
import com.forloop.exceptions.NameAlreadyTakenException;
import com.forloop.model.User;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.*;

@Service
public class UserService {

    private UserDAOJPA userDAOJPA;

    public UserService(UserDAOJPA userDAOJPA) {
        this.userDAOJPA = userDAOJPA;
    }

    public User registration(User user) throws NameAlreadyTakenException {
        userDAOJPA.save(user);
        return user;
    }

    public User login(String username, String password) {

        User userInDB = userDAOJPA.getByName(username);
        if (userInDB != null && checkPw(userInDB, password)) {
            return userInDB;
        }
        return null;
    }

    public String encryptPw(String pw){
        return BCrypt.hashpw(pw, BCrypt.gensalt());
    }


    private boolean checkPw(User user, String pw){
        String storedPw = user.getPassword();
        return BCrypt.checkpw(pw, storedPw);
    }
}
