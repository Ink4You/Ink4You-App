package com.example.ink4youapp.rest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestViaCep {
    val baseUrl = "https://viacep.com.br/ws/"
    fun getInstance(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}
