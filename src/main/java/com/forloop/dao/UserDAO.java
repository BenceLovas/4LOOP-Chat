package com.forloop.dao;

import com.forloop.exceptions.NameAlreadyTakenException;
import com.forloop.model.User;

public interface UserDAO {
    User insertUser(User user) throws NameAlreadyTakenException;

    User getUserByName(String username);
}
