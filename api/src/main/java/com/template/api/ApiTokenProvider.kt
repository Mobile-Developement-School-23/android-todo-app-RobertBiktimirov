package com.template.api

import kotlin.properties.Delegates

interface ApiTokenProvider {
    var token: String

    companion object : ApiTokenProvider {
        override var token: String by Delegates.notNull()
    }
}