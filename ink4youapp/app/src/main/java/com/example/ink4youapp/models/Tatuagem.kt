package com.example.ink4youapp.models

data class Tatuagem (
    val id_tatuagem: Int,
    val titulo: String,
    val local_tatuagem: String,
    val descricao: String,
    val src_imagem: String,
    val id_tatuador: Int
)