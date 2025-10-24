package com.example.survey.data.domain.usecases

import com.example.survey.data.domain.model.Tour
import com.example.survey.data.repository.TourRepository
import kotlinx.coroutines.flow.Flow

class GetBookmarkedTours(private val repository: TourRepository) {
    operator fun invoke(): Flow<List<Tour>> {
        return repository.getBookmarkedTours()
    }
}
