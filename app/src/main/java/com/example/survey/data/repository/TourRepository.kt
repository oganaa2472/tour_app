
package com.example.survey.data.repository
import android.util.Log
import com.example.survey.data.domain.model.Tour
import com.example.survey.data.local.dao.TourDao
import com.example.survey.data.local.entity.TourEntity
import com.example.survey.data.local.prefernces.UserPreferences
import com.example.survey.data.remote.api.TourApiService
import com.example.survey.data.remote.dto.toEntity
import com.example.survey.data.local.entity.toDomain
import com.example.survey.data.remote.network.NetworkModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TourRepository(
    private val tourDao: TourDao,
    private val tourApiService: TourApiService,
    private val userPreferences: UserPreferences,
    private val networkModule: NetworkModule
) {
    fun getAllTours(): Flow<List<Tour>> {
        Log.d("TourRepository", "Fetching data from local database")
        return tourDao.getAllTours().map { entities: List<TourEntity> ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getTourById(id: String): Tour? {
        return tourDao.getTourById(id)?.toDomain()
    }

    fun getBookmarkedTours(): Flow<List<Tour>> {
        return tourDao.getBookmarkedTours().map { entities: List<TourEntity> ->
            entities.map { it.toDomain() }
        }
    }

    fun searchTours(query: String): Flow<List<Tour>> {
        return tourDao.searchTours(query).map { entities: List<TourEntity> ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun bookmarkTour(tourId: String, isBookmarked: Boolean) {
        tourDao.updateBookmarkStatus(tourId, isBookmarked)
    }

    suspend fun syncTours() {
        try {
            Log.d("TourRepository", "Fetching data from the internet")
            val response = tourApiService.getTours()
            if (response.isSuccessful && response.body()?.status == "success") {
                val tourDtos = response.body()?.data?.tours ?: emptyList()
                val baseImageUrl = networkModule.getBaseImageUrl()
                val tourEntities = tourDtos.map { it.toEntity(baseImageUrl) }
                tourDao.insertTours(tourEntities)
                Log.d("TourRepository", "Successfully fetched and stored data from the internet")
            } else {
                Log.e("TourRepository", "API call not successful, will use local data. Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("TourRepository", "Failed to fetch from server, checking local database.", e)
        }
    }

    suspend fun updateLastSyncTime() {
        userPreferences.updateLastSyncTime()
    }

    suspend fun getNewToursSinceLastNotification(): List<Tour> {
        // This would typically check for tours created since last notification
        // For now, return empty list - implement based on your notification logic
        return emptyList()
    }
}
