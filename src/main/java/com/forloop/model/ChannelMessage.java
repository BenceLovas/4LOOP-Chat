package com.forloop.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class ChannelMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String message;

    @ManyToOne
    private User author;

    private Date date;

    @ManyToOne
    private Channel channel;

    @OneToMany(mappedBy = "channelMessage")
    private List<Reply> replies;
}
