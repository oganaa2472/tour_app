package com.example.survey.data.domain.usecases

import com.example.survey.data.domain.model.Tour
import com.example.survey.data.repository.TourRepository

import kotlinx.coroutines.flow.Flow

class SearchToursImpl(
    private val tourRepository: TourRepository
) {
    operator fun invoke(query: String): Flow<List<Tour>> {
        return tourRepository.searchTours(query)
    }
}
