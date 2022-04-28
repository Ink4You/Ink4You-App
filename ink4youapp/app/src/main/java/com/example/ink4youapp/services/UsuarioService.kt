package com.example.ink4youapp.services

import com.example.ink4youapp.models.Tatuador
import com.example.ink4youapp.models.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UsuarioService {

    @POST("/usuarios/cadastro-usuario")
    fun createUser(@Body user: Usuario): Call<Void>

//    @PATCH("/foto/{id}")
//    fun insertPhoto(@Body photo: Multipart): Call<Void>
}