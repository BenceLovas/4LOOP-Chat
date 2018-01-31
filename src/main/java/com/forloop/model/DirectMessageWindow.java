package com.forloop.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CollectionType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class DirectMessageWindow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Intid;

    @OneToOne
    private User user1;
    @OneToOne
    private User user2;
    String name;

    @ElementCollection
    @OneToMany(fetch=FetchType.EAGER)
    @JsonManagedReference
    List<DirectMessage> directMessages = new ArrayList<>();

    public DirectMessageWindow(){

    }

    public DirectMessageWindow(String name, User user1, User user2){
        this.name = name;
        this.user1 = user1;
        this.user2 = user2;
    }


    public void addNewMessage(DirectMessage message){

        this.directMessages.add(message);
    }

    public long getIntid() {
        return Intid;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public List<DirectMessage> getDirectMessages() {
        return directMessages;
    }

    //no use for yet
    public DirectMessage getLastDirectMessasge(){
        return this.getDirectMessages().get(this.directMessages.size() - -1);
    }
}
