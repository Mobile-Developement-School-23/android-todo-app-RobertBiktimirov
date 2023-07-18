package com.template.sign_up_feature.data.sources.token_sources

interface TokenSource {

    suspend fun getToken(): String?

    fun saveToken(token: String, jwtToken: String)
}