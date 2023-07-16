package com.template.sign_up_feature.domain.repository

interface SignRepository {

    suspend fun getToken(): String?

    suspend fun saveToken(token: String)
}