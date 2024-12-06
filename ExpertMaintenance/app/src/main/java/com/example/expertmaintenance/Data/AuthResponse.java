package com.example.expertmaintenance.Data;

import com.example.expertmaintenance.Models.Employe;

public class AuthResponse {
    private boolean success;
    private String message;
    private Employe data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Employe getData() {
        return data;
    }
}
