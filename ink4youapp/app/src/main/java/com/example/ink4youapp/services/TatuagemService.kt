package com.example.ink4youapp.services

import com.example.ink4youapp.models.Tatuagem
import com.example.ink4youapp.models.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TatuagemService {
    @GET("/tatuagens/{id}")
    fun getTattoo(@Path("id") id: Int): Call<Tatuagem>

    @POST("/tatuagens")
    fun createTattoo(@Body tattoo: Tatuagem): Call<Void>
}