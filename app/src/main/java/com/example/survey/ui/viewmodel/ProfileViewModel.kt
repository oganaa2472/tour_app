package com.example.survey.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.survey.data.domain.repository.ImagenRepository
import com.example.survey.data.local.prefernces.UserPreferences
import com.example.survey.data.remote.api.TourApiService
import com.example.survey.data.remote.utils.ImageUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userPreferences: UserPreferences,
    private val tourApiService: TourApiService,
    private val imagenRepository: ImagenRepository,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()


    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearPhotoUpdateSuccess() {
        _uiState.value = _uiState.value.copy(isPhotoUpdated = false)
    }

    fun generateAIImage(prompt: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                // Generate AI image
                val bitmap = imagenRepository.generateImage(prompt)
                Log.d("ProfileViewModel", "AI image generated successfully")
                
                // Get user token
                val token = userPreferences.userToken.first()
                if (token == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "User not authenticated"
                    )
                    return@launch
                }
                
                // Convert bitmap to MultipartBody for upload
                val photoPart = ImageUtils.bitmapToMultipartBody(bitmap, "photo")
                
                // Upload photo to server
                val response = tourApiService.updateProfilePhoto(
                    token = "Bearer $token",
                    photo = photoPart
                )
                
                if (response.isSuccessful && response.body()?.status == "success") {
                    // Update local preferences with new photo URL
                    val newPhotoUrl = response.body()?.data?.user?.photo ?: ""
                    userPreferences.updateUserPhoto(newPhotoUrl)
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isPhotoUpdated = true,
                        generatedImage = bitmap
                    )
                    
                    Log.d("ProfileViewModel", "Photo uploaded successfully to server")
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = response.body()?.status ?: "Failed to upload photo"
                    )
                }
                
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error generating or uploading AI image", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to generate or upload AI image"
                )
            }
        }
    }
}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val isPhotoUpdated: Boolean = false,
    val error: String? = null,
    val generatedImage: Bitmap? = null
)
