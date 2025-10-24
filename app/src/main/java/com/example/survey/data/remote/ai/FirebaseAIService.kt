package com.example.survey.data.remote.ai

import android.content.Context
import com.example.survey.data.domain.repository.ImagenRepository
import com.example.survey.data.remote.repository.FirebaseImagenRepository
import com.google.firebase.ai.type.PublicPreviewAPI
import com.google.firebase.Firebase
import com.google.firebase.ai.ImagenModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.ImagenAspectRatio
import com.google.firebase.ai.type.ImagenGenerationConfig
import com.google.firebase.ai.type.ImagenImageFormat
import com.google.firebase.ai.type.ImagenPersonFilterLevel
import com.google.firebase.ai.type.ImagenSafetyFilterLevel
import com.google.firebase.ai.type.ImagenSafetySettings
import kotlin.concurrent.Volatile

@PublicPreviewAPI
object ServiceLocator {
    @Volatile private var imagenRepository: ImagenRepository? = null
    @Volatile private var model: ImagenModel? = null

    fun getImagenRepository(): ImagenRepository {
        return imagenRepository?: synchronized(this) {
            getImagenModel()
            FirebaseImagenRepository(getImagenModel())
        }
    }

    fun getImagenModel(): ImagenModel {
        // Configure how the Imagen model should generate images
        val config = ImagenGenerationConfig (
            numberOfImages = 1,
            aspectRatio = ImagenAspectRatio.LANDSCAPE_16x9,
            imageFormat = ImagenImageFormat.jpeg(compressionQuality = 100),
//            addWatermark = false
        )

        return model?: synchronized(this) {
            // Initialize the Gemini Developer API backend service
            // For Vertex AI use Firebase.ai(backend = GenerativeBackend.vertexAI())
            val created = Firebase.ai(backend = GenerativeBackend.googleAI()).imagenModel(
                modelName = "imagen-3.0-generate-002",                              // Model version to use
                generationConfig = config,
                safetySettings = ImagenSafetySettings(
                    safetyFilterLevel = ImagenSafetyFilterLevel.BLOCK_LOW_AND_ABOVE, // Filter unsafe outputs
                    personFilterLevel = ImagenPersonFilterLevel.ALLOW_ALL            // Allow images with people
                )
            )
            model = created
            created
        }
    }
}