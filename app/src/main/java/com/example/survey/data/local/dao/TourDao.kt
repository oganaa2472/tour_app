package com.example.survey.data.local.dao

import androidx.room.*
import com.example.survey.data.local.entity.TourEntity

import kotlinx.coroutines.flow.Flow

@Dao
interface TourDao {
    @Query("SELECT * FROM tours ORDER BY createdAt DESC")
    fun getAllTours(): Flow<List<TourEntity>>

    @Query("SELECT * FROM tours WHERE id = :id")
    suspend fun getTourById(id: String): TourEntity?

    @Query("SELECT * FROM tours WHERE isBookmarked = 1 ORDER BY createdAt DESC")
    fun getBookmarkedTours(): Flow<List<TourEntity>>

    @Query("SELECT * FROM tours WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR location LIKE '%' || :query || '%'")
    fun searchTours(query: String): Flow<List<TourEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTour(tour: TourEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTours(tours: List<TourEntity>)

    @Update
    suspend fun updateTour(tour: TourEntity)

    @Delete
    suspend fun deleteTour(tour: TourEntity)

    @Query("UPDATE tours SET isBookmarked = :isBookmarked WHERE id = :tourId")
    suspend fun updateBookmarkStatus(tourId: String, isBookmarked: Boolean)
}