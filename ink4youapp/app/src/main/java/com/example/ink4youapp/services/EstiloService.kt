package com.example.ink4youapp.services

import com.example.ink4youapp.models.Endereco
import com.example.ink4youapp.models.Estilo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EstiloService {

    @GET("/estilo")
    fun getTattoosStyles(): Call<Array<Estilo>>

    @GET("/estilo/populares/")
    fun getTattoosPopularStyles(): Call<List<Estilo>>
}