package com.example.survey.feature.login.domain.repository

import com.example.survey.feature.login.data.remote.dto.request.LoginRequestDto
import com.example.survey.feature.login.data.remote.dto.response.LoginResponseDto
import com.example.survey.feature.login.domain.model.User

interface LoginRepository {
    suspend fun login(request: LoginRequestDto): Result<LoginResponseDto?>
}
