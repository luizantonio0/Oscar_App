package com.oscar.data.model

import io.realm.kotlin.types.RealmObject

class Director(id: Long, nome: String): RealmObject {
    val id: Long = id
    val nome: String = nome

}