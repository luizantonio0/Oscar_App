package com.oscar.service

import com.oscar.config.LocalProperties
import com.oscar.data.dto.request.ConfirmarVotoRequestDTO
import com.oscar.data.dto.request.LoginRequestDTO
import com.oscar.data.dto.response.DiretorResponseDTO
import com.oscar.data.dto.response.FilmeResponseDTO
import com.oscar.data.dto.response.LoginResponseDTO
import com.oscar.data.dto.response.VotacaoDetalhadoResponseDTO
import com.oscar.data.model.Director
import com.oscar.data.model.Movie
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface OscarApi{
    @POST("auth/login")
    suspend fun login(@Body dto: LoginRequestDTO): LoginResponseDTO
    @POST("votos/confirmar")
    suspend fun confirmarVotos(): VotacaoDetalhadoResponseDTO


    @GET("votos/meu")
    suspend fun getVotosConfirmados(@Body votos: ConfirmarVotoRequestDTO): Map<String, Any>
    @GET("filmes")
    suspend fun getFilmes(
        @Header("Authorization") authorization: String
    ): List<FilmeResponseDTO>
    @GET("diretores")
    suspend fun getDiretores(
        @Header("Authorization") authorization: String
    ): List<DiretorResponseDTO>


}

class ApiRequest(var apiUrl: String = LocalProperties.getApiUrl())  {

    var service: OscarApi
    val retrofit: Retrofit

    init {
        this.retrofit = Retrofit.Builder()
            .baseUrl(apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(OscarApi::class.java)
    }

    suspend fun login(dto: LoginRequestDTO): LoginResponseDTO {
        return service.login(dto)
    }

    suspend fun confirmarVotos(): Map<String, Any> {
        TODO("Not yet implemented")
    }

    suspend fun getVotosConfirmados(): VotacaoDetalhadoResponseDTO {
        return service.confirmarVotos()
    }

    suspend fun getFilmes(token: String): List<Movie> {
        val res = service.getFilmes("Bearer $token")
        return res.map { Movie(it.id, it.nome, it.genero, it.foto) }
    }

    suspend fun getDiretores(token: String): List<Director> {
        val res = service.getDiretores("Bearer $token")
        return res.map { Director(it.id, it.nome) }
    }
}