package com.example.survey.data.remote.api

import com.example.survey.data.remote.dto.*
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

interface TourApiService {
    @GET("tours")
    suspend fun getTours(): Response<ToursApiResponse>

    @GET("tours/{id}")
    suspend fun getTourById(@Path("id") id: String): Response<ApiResponse<TourDto>>

    @GET("tours/search")
    suspend fun searchTours(@Query("q") query: String): Response<ApiResponse<List<TourDto>>>

    @POST("users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @PATCH("users/updateMe") // Add this endpoint
    suspend fun updateMe(
        @Header("Authorization") token: String,
        @Body user: UserDto
    ): Response<UpdateUserResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(@Header("Authorization") refreshToken: String): Response<ApiResponse<LoginData>>

    @GET("bookings")
    suspend fun getUserBookings(@Header("Authorization") token: String): Response<ApiResponse<List<BookingDto>>>

    @POST("bookings")
    suspend fun createBooking(
        @Header("Authorization") token: String,
        @Body bookingRequest: CreateBookingRequest
    ): Response<ApiResponse<BookingDto>>

    @PUT("bookings/{id}")
    suspend fun updateBooking(
        @Header("Authorization") token: String,
        @Path("id") bookingId: String,
        @Body updateRequest: UpdateBookingRequest
    ): Response<ApiResponse<BookingDto>>

    @DELETE("bookings/{id}")
    suspend fun cancelBooking(
        @Header("Authorization") token: String,
        @Path("id") bookingId: String
    ): Response<ApiResponse<Unit>>
}

data class CreateBookingRequest(
    val tourId: String,
    val bookingDate: String,
    val numberOfPeople: Int
)

data class UpdateBookingRequest(
    val status: String? = null,
    val numberOfPeople: Int? = null
)

data class UpdateUserResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: LoginData
)
