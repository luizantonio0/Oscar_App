package com.oscar.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class User: RealmObject {
    @PrimaryKey
    var id: Long = 0
    var name: String = ""
    var username: String = ""
    var accessToken: String = ""
    var tokenVotacao: Int = 0
    var isFinished: Boolean = false
}