package com.example.cineapp.Activities.Models;

import java.util.Date;

public class User {

    private String name;
    private String email;
    private Date birth_date;
    private String username;
    private int id;

    public User(String name, String email, Date birth_date, String username) {
        this.name = name;
        this.email = email;
        this.birth_date = birth_date;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
