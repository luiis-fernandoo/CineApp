package com.example.cineapp.Activities.Models;

import java.util.Date;

public class User {
    private  String email;
    private  String nome;
    private String senha;
    private  String cpf;
    public String getCpf() {
        return cpf;
    }


    public User() {
    }

    public User(String email, String nome, String senha, String cpf) {
        this.email = email;
        this.nome = nome;
        this.senha = senha;
        this.cpf = cpf;
    }

    public User(String email, String senha) {
        this.email = email;
        this.senha = senha;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
