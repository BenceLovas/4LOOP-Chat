package com.forloop.dao;

import com.forloop.model.Channel;
import com.forloop.model.ChannelMessage;
import com.forloop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelDAOJPA extends JpaRepository<Channel, Long> {

    List<Channel> findByUserListId(Long id);


}
