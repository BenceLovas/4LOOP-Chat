package com.forloop.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "[user]")
@NamedQueries({
        @NamedQuery(
                name = "findUserByName",
                query = "SELECT u FROM User u WHERE u.name = :name"
        ),
        @NamedQuery(
                name = "findUserById",
                query = "SELECT u FROM User u WHERE u.id = :id"
        ),
        @NamedQuery(
                name = "findUsersByChannel",
                query = "select u FROM User u JOIN u.channels ch WHERE  ch.id= :user_id"
        ),
        @NamedQuery(
                name = "getFriends",
                query = "SELECT u FROM User u JOIN u.userRelations ur WHERE ur.relationState LIKE '%ACCEPTED' AND ur.id = :user_id "
        ),
        @NamedQuery(
                name = "findPendingUsers",
                query = "SELECT u FROM User u JOIN u.userRelations ur WHERE ur.relationState LIKE '%PENDING' AND ur.id = :user_id "
        )
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String password;

    @OneToMany(mappedBy = "sender")
    private List<UserRelation> userRelations;

    private String email;

    @Temporal(TemporalType.DATE)
    private Date regDate;

    @ManyToMany(mappedBy = "userList" , fetch = FetchType.EAGER)
    private List<Channel> channels;

    private Integer reputation;

    public void setName(String name) {
        this.name = name;
    }


    public User() {}


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User(String name, String password, String email, List<Channel> channels, Integer reputation) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.regDate = new Date();
        this.channels = channels;
        this.reputation = reputation;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }
}
