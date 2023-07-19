package com.awakesloth.desarrollo_apps_final_lista_comidas.entities;

public class Usuario {
    private String id;
    private String username;
    private String password;
    private String sqAnswer;

    // Default constructor
    public Usuario() {
        // Required empty constructor for Firebase
    }

    public Usuario(String id, String username, String password, String sqAnswer){
        this.id = id;
        this.username = username;
        this.password = password;
        this.sqAnswer = sqAnswer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSqAnswer() {
        return sqAnswer;
    }

    public void setSqAnswer(String sqAnswer) {
        this.sqAnswer = sqAnswer;
    }
}
