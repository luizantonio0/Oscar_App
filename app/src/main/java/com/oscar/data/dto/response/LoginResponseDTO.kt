package com.oscar.data.dto.response

data class LoginResponseDTO(
    val sucesso: Boolean,
    val token: String,
    val mensagem: String,
    val tokenVotacao: Int
)