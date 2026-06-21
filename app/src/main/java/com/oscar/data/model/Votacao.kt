package com.oscar.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Votacao (@PrimaryKey val id: Long, var filme: Movie?, var diretor: Director?): RealmObject {
}