package com.template.sign_up_feature.data.sources.token_sources

import javax.inject.Inject

class TokenSourceImpl @Inject constructor(

): TokenSource {

    override suspend fun getToken(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun saveToken(token: String) {
        TODO("Not yet implemented")
    }
}