package com.oscar.config

class LocalProperties {
    companion object {
        fun getApiUrl(): String{
            return "http://192.168.15.15:8080"
        }
        fun getApiProfessorUrl(): String{
            return "http://200.236.3.97/"
        }
    }
}