package com.example.ink4youapp.services

import com.example.ink4youapp.models.Tatuagem
import com.example.ink4youapp.models.TatuagemDTO
import com.example.ink4youapp.models.Usuario
import retrofit2.Call
import retrofit2.http.*

interface TatuagemService {
    @GET("/tatuagens/{id}")
    fun getTattoo(@Path("id") id: Int): Call<Tatuagem>

    @GET("/tatuagens/")
    fun getTattoos() : Call<List<TatuagemDTO>>

    @GET("/tatuagens/")
    fun getInstaTattoos() : Call<List<Tatuagem>>

    @GET("/tatuagens/qttd/{qttd}")
    fun getTattoosByQttd(@Path("qttd") qttd: Int) : Call<List<TatuagemDTO>>

    @POST("/tatuagens")
    fun createTattoo(@Body tattoo: Tatuagem): Call<Void>

    @PUT("/tatuagens/{id}")
    fun updateTattoo(@Path("id") id: Int, @Body tattoo: Tatuagem): Call<Tatuagem>

    @GET("/tatuagens/tatuador/{id}")
    fun getTattoosByTattooArtist(@Path("id") id: Int) : Call<List<Tatuagem>>

    @DELETE("tatuagens/{id}")
    fun deleteTattoo(@Path("id") id: Int) : Call<Void>
}