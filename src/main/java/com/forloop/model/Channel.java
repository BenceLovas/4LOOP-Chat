package com.forloop.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Date creationDate;

    private String name;

    @ManyToMany
    private List<User> userList;

    @ManyToOne
    private User creator;

    @ManyToMany(mappedBy = "channels")
    private List<Tag> tags;

    @OneToMany(mappedBy = "channel")
    private List<ChannelMessage> channelMessages;
}
