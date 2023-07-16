package com.template.sign_up_feature.data.repository

import com.template.sign_up_feature.data.sources.token_sources.TokenSource
import com.template.sign_up_feature.domain.repository.SignRepository
import javax.inject.Inject

class SignRepositoryImpl @Inject constructor(
    private val tokenSource: TokenSource
): SignRepository {

    override suspend fun getToken(): String? {
        return tokenSource.getToken()
    }

    override suspend fun saveToken(token: String) {
        tokenSource.saveToken(token)
    }
}