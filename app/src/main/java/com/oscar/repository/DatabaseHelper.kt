package com.oscar.repository

import com.oscar.data.model.User
import com.oscar.data.model.Votacao
import com.oscar.repository.DatabaseManager.realm
import io.realm.kotlin.ext.query

class DatabaseHelper {

    suspend fun deleteUser(user: User){
        realm.write {
            val user = query<User>("id == ${user.id}").first().find()
            if (user != null) {
                delete(user)
            }
        }
    }
    suspend fun saveUser(user: User, token: String, tokenVotacao: Int) {

        if (findUser() != null) return

        realm.write {
            val newUser = User().apply {
                this.id = 1L
                this.username = user.username
                this.accessToken = token
                this.tokenVotacao = tokenVotacao
            }

            copyToRealm(newUser)
        }
    }

    fun findUser(): User? {
        return realm.query<User>("id == $0", 1L).first().find()
    }

    suspend fun updateUser(token: String? = null, tokenVotacao: Int? = null, username: String? = null, isFinished: Boolean? = null) {
        realm.write {
            val userToUpdate = query<User>("id == $0", 1L).first().find()

            userToUpdate?.let { user ->
                user.accessToken = token ?: user.accessToken
                user.tokenVotacao = tokenVotacao ?: user.tokenVotacao
                user.username = username ?: user.username
                user.isFinished = isFinished ?: user.isFinished
            }
        }
    }


    fun findVotacao(): Votacao? {
        return realm.query<Votacao>("id == $0", 1L).first().find()
    }

    suspend fun updateVotacao(votacao: Votacao) {
        realm.write {
            val votacaoToUpdate = query<Votacao>("id == $0", 1L).first().find()

            if (votacaoToUpdate == null) {
                val newVotacao = Votacao(
                    1L,
                    votacao.filme,
                    votacao.diretor,
                    votacao.isFinished
                )
                copyToRealm(newVotacao)
                return@write
            }

            votacaoToUpdate.diretor = votacao.diretor ?: votacaoToUpdate.diretor
            votacaoToUpdate.filme = votacao.filme ?: votacaoToUpdate.filme
            votacaoToUpdate.isFinished = votacao.isFinished
        }
    }

    suspend fun deleteVotacao() {
        realm.write {
            val votacao = query<Votacao>("id == ${1L}").first().find()
            if (votacao != null) {
                delete(votacao)
            }
        }
    }


}