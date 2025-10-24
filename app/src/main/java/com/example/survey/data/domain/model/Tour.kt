package com.example.survey.data.domain.model

import com.example.survey.data.local.entity.TourEntity
import java.util.Date

data class Tour(
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

data class Booking(
    val id: String,
    val tourId: String,
    val userId: String,
    val bookingDate: Date,
    val numberOfPeople: Int,
    val totalPrice: Double,
    val status: BookingStatus,
    val createdAt: Date = Date()
)

// Mapping extension to local entity
fun Tour.toEntity(): TourEntity {
    return TourEntity(
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

enum class BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED
}
