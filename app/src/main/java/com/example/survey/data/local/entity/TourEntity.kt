package com.example.survey.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.survey.data.domain.model.Tour
import java.util.Date

@Entity(tableName = "tours")
data class TourEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val location: String,
    val price: Double,
    val duration: Int, // in days
    val rating: Float,
    val imageUrl: String?,
    val isBookmarked: Boolean = false,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val difficulty: String,
    val maxGroupSize: Int,
    val ratingsQuantity: Int
)

fun TourEntity.toDomain(): Tour {
    return Tour(
        id = id,
        title = title,
        description = description,
        location = location,
        price = price,
        duration = duration,
        rating = rating,
        imageUrl = imageUrl,
        isBookmarked = isBookmarked,
        createdAt = createdAt,
        updatedAt = updatedAt,
        difficulty = difficulty,
        maxGroupSize = maxGroupSize,
        ratingsQuantity = ratingsQuantity
    )
}
