package com.example.survey.feature.login.data.remote.service

import com.example.survey.feature.login.data.remote.dto.request.LoginRequestDto
import com.example.survey.feature.login.data.remote.dto.response.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
    @POST("users/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto
}