package com.github.dieuph.model;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class User {
    private int id;
    private String userName;
    private List<String> roles;

    public User() {
        super();
    }

    public User(int id, String userName, List<String> roles) {
        super();
        this.id = id;
        this.userName = userName;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
