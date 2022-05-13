package com.example.ink4youapp.services

import com.example.ink4youapp.models.Tatuagem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TatuagemService {
    @GET("/tatuagens/{id}")
    fun getTattoo(@Path("id") id: Int): Call<Tatuagem>
}