package com.oscar.data.model

import io.realm.kotlin.types.RealmObject

class Movie: RealmObject {
    var id: Long = 0L
    var nome: String = ""
    var genero:  String = ""
    var foto: String = ""

    constructor(id: Long, nome: String, genero: String, foto: String)
    {
        this.id = id
        this.nome = nome
        this.genero = genero
        this.foto = foto

    }
}