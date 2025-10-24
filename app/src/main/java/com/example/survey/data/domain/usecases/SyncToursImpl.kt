package com.example.survey.data.domain.usecases

import com.example.survey.data.repository.TourRepository

class SyncToursImpl(
    private val tourRepository: TourRepository
) {
    suspend operator fun invoke() {
        tourRepository.syncTours()
        tourRepository.updateLastSyncTime()
    }
}
