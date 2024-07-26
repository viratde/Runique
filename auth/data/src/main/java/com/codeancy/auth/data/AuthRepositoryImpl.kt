package com.codeancy.auth.data

import com.codeancy.auth.domain.AuthRepository
import com.codeancy.core.data.networking.post
import com.codeancy.core.domain.AuthInfo
import com.codeancy.core.domain.SessionStorage
import com.codeancy.core.domain.util.DataError
import com.codeancy.core.domain.util.EmptyResult
import com.codeancy.core.domain.util.Result
import com.codeancy.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
) : AuthRepository {

    override suspend fun register(email: String, password: String): EmptyResult<DataError.Network> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "/register",
            body = RegisterRequest(email, password)
        )
    }

    override suspend fun login(email: String, password: String): EmptyResult<DataError.Network> {
        val result = httpClient.post<LoginRequest, LoginResponse>(
            route = "/login",
            body = LoginRequest(email, password)
        )

        if (result is Result.Success) {
            sessionStorage.set(
                AuthInfo(
                    accessToken = result.data.accessToken,
                    refreshToken = result.data.refreshToken,
                    userId = result.data.userId
                )
            )
        }

        return result.asEmptyDataResult()
    }

}