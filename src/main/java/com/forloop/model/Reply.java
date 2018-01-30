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

    @Temporal(TemporalType.DATE)
    private Date creationDate;

    public Reply() {
    }

    public Reply(ChannelMessage channelMessage, String message, User user) {
        this.channelMessage = channelMessage;
        this.message = message;
        this.user = user;
        this.creationDate = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ChannelMessage getChannelMessage() {
        return channelMessage;
    }

    public void setChannelMessage(ChannelMessage channelMessage) {
        this.channelMessage = channelMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
