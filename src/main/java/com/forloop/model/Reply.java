package com.forloop.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private ChannelMessage channelMessage;

    private String message;

    @ManyToOne
    private User user;

    private Date creationDate;
}
