package com.example.survey.ui.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.survey.data.local.prefernces.UserPreferences
import com.example.survey.data.remote.api.TourApiService
import com.example.survey.data.remote.dto.LoginRequest
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userPreferences: UserPreferences,
    private val tourApiService: TourApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = userPreferences.isLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val currentUser: StateFlow<UserInfo?> = combine(
        userPreferences.userId,
        userPreferences.userEmail,
        userPreferences.userName,
        userPreferences.userPhoto
    ) { userId, email, name, photo ->
        if (userId != null && email != null && name != null) {
            UserInfo(userId, email, name, photo)
        } else null
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val response = tourApiService.login(LoginRequest(email, password))
                Log.d("LoginResponse", response.body().toString())
                if (response.isSuccessful && response.body()?.status == "success") {
                    val loginResponse = response.body()?.data
                    if (loginResponse != null) {
                        userPreferences.saveLoginData(
                            userId = loginResponse.user.id,
                            email = loginResponse.user.email,
                            username = loginResponse.user.username,
                            token = response.body()?.token.toString(),
                            role = loginResponse.user.role,
                            photo = loginResponse.user.photo.toString()
                        )
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoginSuccessful = true
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Invalid response from server"
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = response.body()?.status ?: "Login failed"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Network error"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.logout()
            _uiState.value = AuthUiState()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearLoginSuccess() {
        _uiState.value = _uiState.value.copy(isLoginSuccessful = false)
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val error: String? = null
)

data class UserInfo(
    val id: String,
    val email: String,
    val name: String,
    val photo: String? = null
)
