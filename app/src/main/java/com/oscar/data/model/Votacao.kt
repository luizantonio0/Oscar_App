package com.oscar.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Votacao (): RealmObject {
    @PrimaryKey
    var id: Long = 0L
    var filme: Movie? = null
    var diretor: Director? = null

    constructor( id: Long, filme: Movie?, diretor: Director?): this(){
        this.id = id
        this.filme = filme
        this.diretor = diretor
    }

}