package com.example.survey.work

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

/**
 * Handles uploading a profile photo in the background.
 * Provide the selected image [Uri] via input Data using [KEY_IMAGE_URI].
 */
class UploadProfilePhotoWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    private val TAG = "UploadPhotoWorker"

    override suspend fun doWork(): Result {
        val imageUriString = inputData.getString(KEY_IMAGE_URI)
        if (imageUriString.isNullOrBlank()) return Result.failure()

        val userId = inputData.getString(KEY_USER_ID) // Optional

        return try {
            val imageUri = Uri.parse(imageUriString)
            Log.d(TAG, "Starting upload uri=$imageUri userId=$userId attempt=$runAttemptCount")

            // TODO: Perform real upload using your API client.
            // You can read from the content resolver: applicationContext.contentResolver.openInputStream(imageUri)
            // Then upload bytes to your backend or cloud storage.
            delay(800)
            Log.d(TAG, "Upload success")
            // Optionally return a new photo URL from server via output Data
            Result.success()
        } catch (t: Throwable) {
            Log.e(TAG, "Upload error, will retry: ${t.message}", t)
            Result.retry()
        }
    }

    companion object {
        const val KEY_IMAGE_URI = "imageUri"
        const val KEY_USER_ID = "userId"

        fun buildRequest(
            imageUri: Uri,
            userId: String? = null
        ): OneTimeWorkRequest {
            val input = Data.Builder()
                .putString(KEY_IMAGE_URI, imageUri.toString())
                .apply { if (userId != null) putString(KEY_USER_ID, userId) }
                .build()

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            return OneTimeWorkRequestBuilder<UploadProfilePhotoWorker>()
                .setInputData(input)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
                .build()
        }

        fun enqueue(
            context: Context,
            imageUri: Uri,
            userId: String? = null
        ) {
            val work = buildRequest(imageUri, userId)
            WorkManager.getInstance(context).enqueue(work)
        }
    }
}
