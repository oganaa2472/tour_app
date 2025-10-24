package com.example.survey.data.remote.repository

import android.graphics.Bitmap
import com.example.survey.data.domain.repository.ImagenRepository
import com.google.firebase.ai.ImagenModel
import com.google.firebase.ai.type.ImagenGenerationResponse
import com.google.firebase.ai.type.ImagenInlineImage
import com.google.firebase.ai.type.PublicPreviewAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@PublicPreviewAPI
class FirebaseImagenRepository(
    private val model: ImagenModel?
): ImagenRepository {
    override suspend fun generateImage(prompt: String): Bitmap = withContext(Dispatchers.IO) {
        val response: ImagenGenerationResponse<ImagenInlineImage>? =
            model?.generateImages(prompt = prompt)
        val image = response?.images?.firstOrNull() ?: throw IllegalStateException("No images generated")
        image.asBitmap()
    }
}