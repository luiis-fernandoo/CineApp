package com.example.cineapp.Activities.Models;

public class SaveList {

    private int id;
    private int film_id;
    private int watchList_id;

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

    public SaveList(int film_id, int watchList_id) {
        this.film_id = film_id;
        this.watchList_id = watchList_id;
    }
}