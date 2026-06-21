package com.oscar.service

import android.content.Context
import android.content.Intent
import com.oscar.activity.LoginActivity
import com.oscar.config.LocalProperties
import com.oscar.data.dto.request.ConfirmarVotoRequestDTO
import com.oscar.data.dto.request.LoginRequestDTO
import com.oscar.data.dto.response.DiretorResponseDTO
import com.oscar.data.dto.response.FilmeResponseDTO
import com.oscar.data.dto.response.LoginResponseDTO
import com.oscar.data.dto.response.VotacaoDetalhadoResponseDTO
import com.oscar.data.dto.response.VotacaoResponseDTO
import com.oscar.data.model.Director
import com.oscar.data.model.Movie
import com.oscar.data.model.Votacao
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import kotlin.jvm.java

interface OscarApi{
    @POST("auth/login")
    suspend fun login(@Body dto: LoginRequestDTO): LoginResponseDTO
    @POST("votos/confirmar")
    suspend fun confirmarVotos(@Body votos: ConfirmarVotoRequestDTO, @Header("Authorization") authorization: String): VotacaoResponseDTO
    @GET("votos/meu")
    suspend fun getVotosConfirmados(): VotacaoDetalhadoResponseDTO
    @GET("filmes")
    suspend fun getFilmes(
        @Header("Authorization") authorization: String
    ): List<FilmeResponseDTO>
    @GET("diretores")
    suspend fun getDiretores(
        @Header("Authorization") authorization: String
    ): List<DiretorResponseDTO>


}

class ApiRequest(context: Context , var apiUrl: String = LocalProperties.getApiUrl())  {

    var service: OscarApi
    val retrofit: Retrofit
    private val client = OkHttpClient.Builder()
        .addInterceptor(ApiMiddlewareInterceptor(context))
        .build()

    init {
        this.retrofit = Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(OscarApi::class.java)
    }

    suspend fun login(dto: LoginRequestDTO): LoginResponseDTO {
        return service.login(dto)
    }

    suspend fun confirmarVotos(votacao: Votacao, tokenVotacao: Int, token: String): VotacaoResponseDTO {
        return service.confirmarVotos(
            ConfirmarVotoRequestDTO(
                votacao.filme!!.id,
                votacao.diretor!!.id,
                tokenVotacao
            ),
            "Bearer $token"
        )
    }

    suspend fun getVotosConfirmados(): VotacaoDetalhadoResponseDTO {
        return service.getVotosConfirmados()
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

class ApiMiddlewareInterceptor(val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val response = chain.proceed(request)

        when (response.code()) {
            401, 403 -> {
                response.close()
                context.startActivity(Intent(context, LoginActivity::class.java))
                throw ApiUnauthorizedException()
            }
        }

        return response
    }
}
class ApiUnauthorizedException : RuntimeException(
    "Usuário não autorizado ou sessão expirada."
)