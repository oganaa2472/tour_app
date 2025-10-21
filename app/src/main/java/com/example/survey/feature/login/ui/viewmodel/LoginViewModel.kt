package com.example.survey.feature.login.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.survey.feature.login.data.local.TokenManager
import com.example.survey.feature.login.data.remote.dto.request.LoginRequestDto
import com.example.survey.feature.login.domain.repository.LoginRepository
import com.example.survey.feature.login.ui.state.LoginUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val userRepository: LoginRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun login() {
        viewModelScope.launch {
           _uiState.update {
               it.copy(isLoading = true)
           }

            val result = withContext(Dispatchers.IO) {
                userRepository.login(
                    LoginRequestDto(
                        email = _uiState.value.email,
                        password = _uiState.value.password
                    )
                )
            }
            result.onSuccess { token ->
                    _uiState.update {
                        it.copy(

                            isLoading = false,
                            success = true,
                            errorMessage = null,
                            token = token?.token.toString(),
                            user = token,

                        )
                    }
                    tokenManager.saveToken(token = token?.token.toString())
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Unknown error"
                        )
                    }
                }

        }
    }

    fun onEmailChange(value: String) {
        _uiState.update {
            it.copy(email = value)
        }
    }
    fun onPasswordChange(value: String) {
        _uiState.update {
            it.copy(password = value)
        }
    }

}