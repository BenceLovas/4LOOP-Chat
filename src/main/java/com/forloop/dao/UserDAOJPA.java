package com.forloop.dao;

import com.forloop.model.Channel;
import com.forloop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDAOJPA extends JpaRepository<User, Long> {

    User getByName(String name);
}
