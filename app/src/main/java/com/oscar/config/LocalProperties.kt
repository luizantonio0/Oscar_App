package com.oscar.config

class LocalProperties {
    companion object {
        fun getApiUrl(): String{
            return "https://oscar-api-production-4735.up.railway.app"
        }
        fun getApiProfessorUrl(): String{
            return "http://200.236.3.97/"
        }
    }
}