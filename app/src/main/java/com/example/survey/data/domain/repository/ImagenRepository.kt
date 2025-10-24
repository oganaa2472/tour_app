package com.example.survey.data.domain.repository

import android.graphics.Bitmap

interface ImagenRepository {
    suspend fun generateImage(prompt: String): Bitmap
}
