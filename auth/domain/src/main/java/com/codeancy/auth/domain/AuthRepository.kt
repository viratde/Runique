package com.codeancy.auth.domain

import com.codeancy.core.domain.util.DataError
import com.codeancy.core.domain.util.EmptyResult

interface AuthRepository {

    suspend fun register(email: String, password: String): EmptyResult<DataError.Network>

    suspend fun login(email: String, password: String): EmptyResult<DataError.Network>
}