package com.example.survey.feature.login.data.remote.dto.response

data class LoginResponseDto(
    val status: String,
    val token: String,
    val data: DataDto
)