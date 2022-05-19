package com.example.ink4youapp.services

import com.example.ink4youapp.models.Tatuador
import com.example.ink4youapp.models.Usuario
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface UsuarioService {
    @GET("/usuarios/login/{email}/{senha}")
    fun auth(@Path("email") email: String, @Path("senha") senha: String): Call<Usuario>

    @POST("/usuarios/cadastro-usuario")
    fun createUser(@Body user: Usuario): Call<Void>

//    @Multipart
//    @PATCH("/usuarios/foto/7")
//    fun insertPhoto(@Part("foto") foto: RequestBody): Call<Void>
}