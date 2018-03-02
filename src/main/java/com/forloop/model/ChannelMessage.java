package com.forloop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import javax.persistence.*;
import java.util.ArrayList;
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

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne
    @JsonBackReference
    private Channel channel;


    public ChannelMessage() {
    }

    public ChannelMessage(String message, User author, Channel channel) {
        this.message = message;
        this.author = author;
        this.date = new Date();
        this.channel = channel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

}
