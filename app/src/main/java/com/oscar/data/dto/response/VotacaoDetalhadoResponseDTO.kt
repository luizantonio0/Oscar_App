package com.oscar.data.dto.response

data class VotacaoDetalhadoResponseDTO(
    val filme: FilmeInfo,
    val diretor: DiretorInfo
) {
    data class FilmeInfo(
        val id: Long,
        val nome: String,
        val genero: String,
        val foto: String
    )

    data class DiretorInfo(
        val id: Long,
        val nome: String
    )
}