package com.oscar.data.model

import io.realm.kotlin.types.RealmObject

class Director(): RealmObject {
    var id: Long = 0L
    var nome: String = ""

    constructor(id: Long, nome: String) : this() {
        this.id = id
        this.nome = nome
    }

}