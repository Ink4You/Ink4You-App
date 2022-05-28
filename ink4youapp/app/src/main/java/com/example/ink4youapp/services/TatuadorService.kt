package com.example.ink4youapp.services

import com.example.ink4youapp.models.Tatuador
import com.example.ink4youapp.models.TatuadorDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Body

import retrofit2.http.POST

interface TatuadorService {
    @GET("/tatuadores/{id}")
    fun getTattooArtist(@Path("id") id :Int) : Call<Tatuador>

    @GET("/tatuadores/")
    fun getTattooArtists() : Call<List<TatuadorDTO>>

    @GET("/tatuadores/qttd/{qttd}")
    fun getTattooArtistsByQttd(@Path("qttd") qttd: Int) : Call<List<TatuadorDTO>>

    @GET("/tatuadores/login/{email}/{senha}")
    fun auth(@Path("email") email: String, @Path("senha") senha: String): Call<Tatuador>

    @POST("/tatuadores/cadastro-tatuador")
    fun createTattooArtist(@Body tattooArtist: Tatuador): Call<Void>
}