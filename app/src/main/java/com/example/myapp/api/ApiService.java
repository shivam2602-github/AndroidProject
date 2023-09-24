package com.example.myapp.api;
import com.example.myapp.models.University;

import retrofit2.Call ;
import retrofit2.http.GET;

import java.util.List ;
public interface ApiService {
    @GET("/search")
    Call<List<University>> getUniversityData();
}
