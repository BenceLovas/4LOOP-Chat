package com.forloop.dao;

import com.forloop.model.ChannelMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelMessageDAOJPA extends JpaRepository<ChannelMessage, Long> {

    List<ChannelMessage> findAllByChannelId(Long id);

    ChannelMessage findTopByChannelIdOrderByIdDesc(Long id);
}
