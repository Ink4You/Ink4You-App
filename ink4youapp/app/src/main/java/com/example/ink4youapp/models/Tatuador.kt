package com.example.ink4youapp.models

import retrofit2.Call
import retrofit2.http.GET
import java.time.LocalDate

data class Tatuador(
    val id_tatuador:Int,
    val nome: String,
    val username: String,
    val data_nascimento: String,
    val cnpj: String,
    val cep: String,
    val logradouro: String,
    val numero_logradouro: String,
    val telefone: String,
    val email: String,
    val senha: String,
    val conta_instagram: String,
    val foto_perfil: String,
    val uf: String,
    val idade: Int,
    val sobre: String
)