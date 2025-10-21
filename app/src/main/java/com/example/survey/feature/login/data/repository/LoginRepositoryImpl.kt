package com.example.survey.feature.login.data.repository

import com.example.survey.feature.login.data.remote.dto.request.LoginRequestDto
import com.example.survey.feature.login.data.remote.dto.response.LoginResponseDto
import com.example.survey.feature.login.data.remote.service.UserService
import com.example.survey.feature.login.domain.repository.LoginRepository

class LoginRepositoryImpl( private val userServices: UserService) : LoginRepository {
    override suspend fun login(request: LoginRequestDto): Result<LoginResponseDto> {
        return runCatching {
            userServices.login(request)
        }
    }
}
