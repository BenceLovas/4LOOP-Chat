package com.forloop.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "getAllChannelMessagesDescendingByChannelId",
                    query = "SELECT cm FROM ChannelMessage cm WHERE cm.channel.id = :channelId " +
                            "ORDER BY cm.id DESC")
})
public class ChannelMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String message;

    @ManyToOne
    private User author;

    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne
    private Channel channel;

    @OneToMany(mappedBy = "channelMessage")
    private List<Reply> replies;

    public ChannelMessage() {
    }

    public ChannelMessage(String message, User author, Channel channel) {
        this.message = message;
        this.author = author;
        this.date = new Date();
        this.channel = channel;
        this.replies = new ArrayList<>();
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

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }
}
