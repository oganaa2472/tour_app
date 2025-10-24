package com.example.survey.data.domain.repository

import com.example.survey.data.domain.model.Tour
import kotlinx.coroutines.flow.Flow

interface TourRepository {
    fun getAllTours(): Flow<List<Tour>>
    suspend fun getTourById(id: String): Tour?
    fun getBookmarkedTours(): Flow<List<Tour>>
    fun searchTours(query: String): Flow<List<Tour>>
    suspend fun bookmarkTour(tourId: String, isBookmarked: Boolean)
    suspend fun syncTours()
    suspend fun updateLastSyncTime()
    suspend fun getNewToursSinceLastNotification(): List<Tour>
}
