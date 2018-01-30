package com.forloop.model;

import javax.persistence.*;

@Entity
public class UserRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private User sender;

    @OneToOne
    private User receiver;

    @Enumerated(EnumType.STRING)
    private RelationState relationState;

    public UserRelation(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.relationState = RelationState.PENDING;
    }

    public UserRelation() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public RelationState getRelationState() {
        return relationState;
    }

    public void setRelationState(RelationState relationState) {
        this.relationState = relationState;
    }
}


