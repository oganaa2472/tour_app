package com.example.survey.work

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Helper functions to schedule WorkManager tasks from anywhere in the app.
 */
object WorkSchedulers {

    private const val UNIQUE_TOUR_SYNC = "unique_tour_sync"

    /**
        Call once on app start (e.g., in MainActivity.onCreate) to keep tours fresh.
     */
    fun schedulePeriodicTourSync(
        context: Context,
        repeatHours: Long = 12,
        flexHours: Long = 2
    ) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val work = PeriodicWorkRequestBuilder<SyncToursWorker>(
            repeatHours, TimeUnit.HOURS,
            flexHours, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            UNIQUE_TOUR_SYNC,
            ExistingPeriodicWorkPolicy.KEEP,
            work
        )
    }

    /**
     * Enqueue one-off profile photo upload. Typically called after user picks an image.
     */
    fun enqueueProfilePhotoUpload(
        context: Context,
        imageUri: android.net.Uri,
        userId: String? = null
    ) {
        val request = UploadProfilePhotoWorker.buildRequest(imageUri, userId)
        WorkManager.getInstance(context).enqueueUniqueWork(
            // Unique by user to avoid piling up many uploads; change if you want multiple
            "upload_profile_photo_${userId ?: "anon"}",
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            request
        )
    }
}
