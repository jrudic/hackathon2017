package com.projectx.jovanrudic.mhydrabanking.model;

/**
 * Created by urossimic on 6/30/17.
 */

public class User {
    //just empty user model for testing
    private String name;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
