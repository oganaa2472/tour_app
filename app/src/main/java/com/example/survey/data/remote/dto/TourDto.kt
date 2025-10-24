package com.example.survey.data.remote.dto

import com.example.survey.data.local.entity.TourEntity
import com.google.gson.annotations.SerializedName

// Main response structure for the tours list endpoint
data class ToursApiResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("results")
    val results: Int,
    @SerializedName("data")
    val data: TourListData
)

// Handles the nested "data" object containing the list of tours
data class TourListData(
    @SerializedName("data")
    val tours: List<TourDto>
)

data class TourDto(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("maxGroupSize")
    val maxGroupSize: Int,
    @SerializedName("difficulty")
    val difficulty: String,
    @SerializedName("ratingsAverage")
    val ratingsAverage: Double,
    @SerializedName("ratingsQuantity")
    val ratingsQuantity: Int,
    @SerializedName("price")
    val price: Double,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("imageCover")
    val imageCover: String,
    @SerializedName("images")
    val images: List<String>,
    @SerializedName("startDates")
    val startDates: List<String>,
    @SerializedName("guides")
    val guides: List<GuideDto>,
    @SerializedName("locations")
    val locations: List<LocationDto>
)

data class GuideDto(
    @SerializedName("_id")
    val id: String,
    @SerializedName("username")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("role")
    val role: String
)

data class LocationDto(
    @SerializedName("_id")
    val id: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("coordinates")
    val coordinates: List<Double>,
    @SerializedName("day")
    val day: Int?
)

data class BookingDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("tour_id")
    val tourId: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("booking_date")
    val bookingDate: String,
    @SerializedName("number_of_people")
    val numberOfPeople: Int,
    @SerializedName("total_price")
    val totalPrice: Double,
    @SerializedName("status")
    val status: String,
    @SerializedName("created_at")
    val createdAt: String
)

data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

data class LoginResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: LoginData
)

data class LoginData(
    @SerializedName("user")
    val user: UserDto
)

data class UserDto(
    @SerializedName("_id")
    val id: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("photo")
    val photo: String?,
)

data class ApiResponse<T>(
    @SerializedName("success")
    val status: String?,
    @SerializedName("data")
    val data: T?,
    @SerializedName("message")
    val message: String?
)

fun TourDto.toEntity(baseImageUrl: String): TourEntity {
    return TourEntity(
        id = id,
        title = name,
        description = description,
        location = locations.firstOrNull()?.description ?: "",
        price = price,
        duration = duration,
        rating = ratingsAverage.toFloat(),
        imageUrl = baseImageUrl + imageCover,
        isBookmarked = false,
        createdAt = java.util.Date(), // TODO: Parse from string
        updatedAt = java.util.Date(), // TODO: Parse from string
        difficulty = difficulty,
        maxGroupSize = maxGroupSize,
        ratingsQuantity = ratingsQuantity
    )
}