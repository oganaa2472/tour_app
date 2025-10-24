package com.example.survey.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey
    val id: String,
    val tourId: String,
    val userId: String,
    val bookingDate: Date,
    val numberOfPeople: Int,
    val totalPrice: Double,
    val status: BookingStatus,
    val createdAt: Date = Date()
)

enum class BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED
}