package com.example.survey.feature.login.data.remote.dto.response

data class UserDto(
    val role: String,
    val photo: String,
    val _id: String,
    val username: String,
    val email: String,
    val __v: Int
)