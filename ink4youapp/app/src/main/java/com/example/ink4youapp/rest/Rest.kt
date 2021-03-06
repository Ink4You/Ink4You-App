package com.example.ink4youapp.rest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Rest {
    val baseUrl = "http://54.172.224.141:8080/"
  
    fun getInstance() : Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}