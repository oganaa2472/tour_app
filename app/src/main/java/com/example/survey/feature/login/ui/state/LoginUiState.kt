package com.example.survey.feature.login.ui.state

import com.example.survey.feature.login.data.remote.dto.response.LoginResponseDto

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false,
    val user: LoginResponseDto? = null,
    val token:String? = null
)