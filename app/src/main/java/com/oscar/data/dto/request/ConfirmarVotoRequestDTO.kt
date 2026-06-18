package com.oscar.data.dto.request

data class ConfirmarVotoRequestDTO(
    val filmeId: Long,
    val diretorId: Long,
    val tokenVotacao: Int
)
