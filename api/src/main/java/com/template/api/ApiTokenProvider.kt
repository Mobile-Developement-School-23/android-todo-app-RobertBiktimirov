package com.template.api

interface ApiTokenProvider {
    var token: String

    companion object : ApiTokenProvider {
        override var token: String = ""
    }
}