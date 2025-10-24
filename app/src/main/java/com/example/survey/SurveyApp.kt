package com.example.survey

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.example.survey.work.WorkSchedulers

class SurveyApp : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        // Schedule periodic background sync for tours once on app start
        WorkSchedulers.schedulePeriodicTourSync(this)
    }

    // WorkManager 2.10+ uses a Kotlin property instead of a function
    override val workManagerConfiguration: Configuration by lazy {
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()
    }
}
