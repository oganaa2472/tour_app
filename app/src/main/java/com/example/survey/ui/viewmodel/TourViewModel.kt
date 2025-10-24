package com.example.survey.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.survey.data.domain.model.Tour
import com.example.survey.data.domain.usecases.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TourViewModel(
    private val getToursUseCase: GetToursImpl,
    private val searchToursUseCase: SearchToursImpl,
    private val bookmarkTourUseCase: BookmarkTourImpl,
    private val syncToursUseCase: SyncToursImpl,
    private val getBookmarkedToursUseCase: GetBookmarkedTours // Add this line
) : ViewModel() {

    private val _uiState = MutableStateFlow(TourUiState())
    val uiState: StateFlow<TourUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _bookmarkedTours = MutableStateFlow<List<Tour>>(emptyList())
    val bookmarkedTours: StateFlow<List<Tour>> = _bookmarkedTours.asStateFlow()

    init {
        refreshTours()
        observeSearchQuery()
        loadBookmarkedTours()
    }

    private fun loadBookmarkedTours() {
        viewModelScope.launch {
            getBookmarkedToursUseCase().collect { tours ->
                _bookmarkedTours.value = tours
            }
        }
    }

    private fun loadTours() {
        viewModelScope.launch {
            getToursUseCase()
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
                .collect { tours ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        tours = tours,
                        error = null
                    )
                }
        }
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQuery
                .debounce(300) // Wait 300ms after user stops typing
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isBlank()) {
                        loadTours()
                    } else {
                        searchTours(query)
                    }
                }
        }
    }

    private fun searchTours(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            searchToursUseCase(query)
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
                .collect { tours ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        tours = tours,
                        error = null
                    )
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleBookmark(tourId: String) {
        viewModelScope.launch {
            val currentTours = _uiState.value.tours
            val tour = currentTours.find { it.id == tourId }
            tour?.let {
                bookmarkTourUseCase(tourId, !it.isBookmarked)
            }
        }
    }

    fun refreshTours() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                syncToursUseCase()
                loadTours()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class TourUiState(
    val isLoading: Boolean = false,
    val tours: List<Tour> = emptyList(),
    val error: String? = null
)
