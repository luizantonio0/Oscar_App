package com.oscar.repository

import com.oscar.data.model.User
import com.oscar.data.model.Votacao
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

object DatabaseManager {
    val realm: Realm by lazy {
        val config = RealmConfiguration.Builder(
            schema = setOf(User::class, Votacao::class)
        )
            .name("meu_app.realm")
            .schemaVersion(2) // Controle de versões do banco
            .build()

        Realm.open(config)
    }
}