package com.example.ink4youapp.models

data class Usuario (
    val id_usuario: Int?,
    val nome: String?,
    val data_nascimento: String?,
    val cpf: String?,
    val cep: String?,
    val telefone: String?,
    val email: String?,
    val senha: String?,
    val foto_perfil: String?,
    val idade: Int?
)