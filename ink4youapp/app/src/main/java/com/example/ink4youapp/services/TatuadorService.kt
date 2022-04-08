package com.example.ink4youapp.services

import com.example.ink4youapp.models.Tatuador
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TatuadorService {

    @GET("/tatuadores/login/{email}/{senha}")
    fun auth(@Path("email") email: String, @Path("senha") senha: String): Call<Tatuador>

}