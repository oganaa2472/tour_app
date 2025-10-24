package com.example.survey.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

/**
 * Periodically syncs tours or any server-driven content.
 * Replace the TODO section with your repository/API call.
 */
class SyncToursWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    private val TAG = "SyncToursWorker"

    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "Start sync, attempt=$runAttemptCount")

            // TODO: Inject and call your repository to refresh tours from network
//             e.g., TourRepository.refreshTours()
            // Simulate some work
            delay(500)
            Log.d(TAG, "Sync success")
            Result.success()
        } catch (t: Throwable) {
            Log.e(TAG, "Sync error, will retry: ${t.message}", t)

            // Let WorkManager retry according to backoff policy
            Result.retry()
        }
    }
}
