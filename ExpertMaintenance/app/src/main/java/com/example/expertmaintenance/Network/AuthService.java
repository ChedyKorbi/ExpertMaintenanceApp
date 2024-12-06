package com.example.expertmaintenance.Network;

import com.example.expertmaintenance.Data.AuthRequest;
import com.example.expertmaintenance.Data.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;





public interface AuthService {
    @POST("auth.php")  // Assure-toi que le chemin est correct
    Call<AuthResponse> login(@Body AuthRequest authRequest);
}
