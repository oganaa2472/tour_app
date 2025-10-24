package com.example.survey.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.survey.ui.viewmodel.TourViewModel
import com.tourexplorer.app.ui.component.TourCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    onTourClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: TourViewModel
) {
    val bookmarkedTours by viewModel.bookmarkedTours.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bookmarked Tours") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (bookmarkedTours.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No bookmarked tours",
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(bookmarkedTours) { tour ->
                    TourCard(
                        tour = tour,
                        onClick = { onTourClick(tour.id) },
                        onBookmarkClick = { viewModel.toggleBookmark(tour.id) }
                    )
                }
            }
        }
    }
}
