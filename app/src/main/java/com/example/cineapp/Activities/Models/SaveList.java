package com.example.cineapp.Activities.Models;

public class SaveList {

    private int id;
    private int film_id;
    private int watchList_id;
    private int user_id;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getFilm_id() {
        return film_id;
    }

    public void setFilm_id(int film_id) {
        this.film_id = film_id;
    }

    public int getWatchList_id() {
        return watchList_id;
    }

    public void setWatchList_id(int watchList_id) {
        this.watchList_id = watchList_id;
    }

    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public SaveList(int film_id, int watchList_id, int user_id) {
        this.film_id = film_id;
        this.watchList_id = watchList_id;
        this.user_id = user_id;
    }
}
