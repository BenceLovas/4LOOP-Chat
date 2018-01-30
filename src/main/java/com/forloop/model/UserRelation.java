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
}
