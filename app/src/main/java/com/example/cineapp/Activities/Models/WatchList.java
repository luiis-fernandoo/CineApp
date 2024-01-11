package com.example.cineapp.Activities.Models;

public class WatchList {

    private int id;
    private String name;
    private int user_id;
    private int film_id;

    public WatchList(String name, int user_id, int film_id) {
        this.name = name;
        this.user_id = user_id;
        this.film_id = film_id;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFilm_id() {
        return film_id;
    }

    public void setFilm_id(int film_id) {
        this.film_id = film_id;
    }


}
