package com.oscar.data.model

class Movie {
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