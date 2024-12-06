package com.example.expertmaintenance.Models;

public class Employe {
    private int id;
    private String login;
    private String pwd;
    private String prenom;
    private String name;  // Change field name from 'nom' to 'name'
    private String email;
    private boolean actif;

    // Constructor
    public Employe(int id, String login, String pwd, String prenom, String name, String email, boolean actif) {
        this.id = id;
        this.login = login;
        this.pwd = pwd;
        this.prenom = prenom;
        this.name = name;  // Use 'name' here instead of 'nom'
        this.email = email;
        this.actif = actif;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPwd() {
        return pwd;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getName() {
        return name;  // Use 'getName' instead of 'getNom'
    }

    public String getEmail() {
        return email;
    }

    public boolean isActif() {
        return actif;
    }
}
