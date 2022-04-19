package com.example.ink4youapp.services

import com.example.ink4youapp.models.Endereco
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EnderecoService {

    @GET("{zipCode}/json/")
    fun getAdressInfos(@Path("zipCode") zipCode: String): Call<Endereco>

}