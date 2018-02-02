package com.forloop.model;

import javax.persistence.*;

@Entity
public class UserRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User sender;

    private long receiver_id;

    @Enumerated(EnumType.STRING)
    private RelationState relationState;

    public UserRelation(User sender, long receiver_id) {
        this.sender = sender;
        this.receiver_id = receiver_id;
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

    public long getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(long receiver_id) {
        this.receiver_id = receiver_id;
    }

    public RelationState getRelationState() {
        return relationState;
    }

    public void setRelationState(RelationState relationState) {
        this.relationState = relationState;
    }
}


