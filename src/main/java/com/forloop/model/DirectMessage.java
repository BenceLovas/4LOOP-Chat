package com.forloop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.nio.MappedByteBuffer;

@Entity
public class DirectMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonManagedReference
    long id;

    @OneToOne()
    @JsonBackReference
    //@JsonProperty("messeageOwner")
    private User messageOwner;
    @JsonManagedReference
    private String message;
    @OneToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private DirectMessageWindow directMessageWindow;


    public User getMessageOwner() {
        return messageOwner;
    }

    public DirectMessage(){

    }

    public DirectMessageWindow getDirectMessageWindow() {
        return directMessageWindow;
    }



    public DirectMessage(User user, String message){
        this.message = message;
        this.messageOwner = user;
    }

    public void sendMessage(DirectMessageWindow chatwindow){
        chatwindow.addNewMessage(this);
        this.directMessageWindow = chatwindow;
    }

    public String toString() {
        return "DirectMessage{" +
                "id=" + id +
                ", directMessageWindow=" + directMessageWindow +
                ", messageOwner=" + messageOwner +
                ", message='" + message + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
