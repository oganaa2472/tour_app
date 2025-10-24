package com.example.survey.data.domain.usecases

import com.example.survey.data.repository.TourRepository

class BookmarkTourImpl(
    private val tourRepository: TourRepository
) {
    suspend operator fun invoke(tourId: String, isBookmarked: Boolean) {
        tourRepository.bookmarkTour(tourId, isBookmarked)
    }
}