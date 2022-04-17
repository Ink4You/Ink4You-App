package com.example.ink4youapp.services

import com.example.ink4youapp.models.Tatuador
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Body

import retrofit2.http.POST

interface TatuadorService {
    @GET("/tatuadores/login/{email}/{senha}")
    fun auth(@Path("email") email: String, @Path("senha") senha: String): Call<Tatuador>

    @POST("/tatuadores/cadastro-tatuador")
    fun createTattooArtist(@Body tattooArtist: Tatuador): Call<Void>
}