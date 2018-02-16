package com.forloop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.*;

@Entity
@NamedQueries({
        //AscByName, DescByName
        @NamedQuery(name = "getAllChannelsAscByName", query = "SELECT c FROM Channel c ORDER BY c.name"),
        @NamedQuery(name = "getAllChannelsDescByName", query = "SELECT c FROM Channel c ORDER BY c.name DESC"),


        //AscByCreationDate, DescByCreationDate
        @NamedQuery(name = "getAllChannelsAscByCreationDate",
                    query = "SELECT c FROM Channel c ORDER BY c.creationDate"),
        @NamedQuery(name = "getAllChannelsDescByCreationDate",
                query = "SELECT c FROM Channel c ORDER BY c.creationDate DESC"),

        @NamedQuery(name="getAllChannels", query = "SELECT channel FROM Channel channel"),
        //SearchLikeName
        @NamedQuery(name = "getChannelsLikeName", query = "SELECT c FROM Channel c WHERE c.name LIKE CONCAT('%', :name, '%')"),
        //AscByName
        @NamedQuery(name = "getChannelsByUserId",
                    query = "SELECT c FROM Channel c INNER JOIN c.userList u WHERE u.id = :userId ORDER BY c.name"),
        @NamedQuery(name = "getChannelsByTagName",
                    query = "SELECT c FROM Channel c INNER JOIN c.tags t WHERE t.name = :tagName")
})
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<User> userList;

    @ManyToOne
    private User creator;

    @ManyToMany
    @JsonManagedReference
    private List<Tag> tags;

    @OneToMany(mappedBy = "channel", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ChannelMessage> channelMessages;

    public Channel() {
    }

    public Channel(String name, User creator) {
        this.name = name;
        this.creationDate = new Date();
        this.userList = new HashSet<>();
        this.creator = creator;
        this.tags = new ArrayList<>();
        this.channelMessages = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUserList() {
        return userList;
    }

    public void setUserList(Set<User> userList) {
        this.userList = userList;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<ChannelMessage> getChannelMessages() {
        return channelMessages;
    }

    public void setChannelMessages(List<ChannelMessage> channelMessages) {
        this.channelMessages = channelMessages;
    }

    public void addUserToChannel(User user) {
        userList.add(user);
        user.addChannel(this);
    }

    public void addMessageToChannel(ChannelMessage channelMessage) {
        this.channelMessages.add(channelMessage);
    }
}
