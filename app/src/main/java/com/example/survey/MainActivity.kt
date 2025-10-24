package com.example.survey

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.survey.data.domain.repository.ImagenRepository
import com.example.survey.data.domain.usecases.*
import com.example.survey.data.local.database.TourDatabase
import com.example.survey.data.local.prefernces.UserPreferences
import com.example.survey.data.remote.ai.ServiceLocator
import com.example.survey.data.remote.network.NetworkModule
import com.example.survey.data.repository.TourRepository
import com.example.survey.ui.navigation.Screen
import com.example.survey.ui.viewmodel.AuthViewModel
import com.example.survey.ui.viewmodel.ProfileViewModel
import com.example.survey.ui.viewmodel.TourViewModel
import com.example.survey.ui.navigation.TourNavigation
import com.google.firebase.ai.type.PublicPreviewAPI


class MainActivity : ComponentActivity() {
    @OptIn(PublicPreviewAPI::class)
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val (tourViewModel, authViewModel, profileViewModel) = remember {
                        val database = TourDatabase.getDatabase(this)
                        val tourDao = database.tourDao()
                        val bookingDao = database.bookingDao()
                        val userPreferences = UserPreferences(this)
                        val networkModule = NetworkModule()
                        val tourApiService = networkModule.provideTourApiService(networkModule.provideRetrofit(networkModule.provideOkHttpClient()))
                        val tourRepository =
                            TourRepository(
                                tourDao,
                                tourApiService,
                                userPreferences,
                                networkModule
                            )

                        // Use cases
                        val getToursUseCase = GetToursImpl(tourRepository)
                        val searchToursUseCase = SearchToursImpl(tourRepository)
                        val bookmarkTourUseCase = BookmarkTourImpl(tourRepository)
                        val syncToursUseCase = SyncToursImpl(tourRepository)
                        val getBookmarkedToursUseCase = GetBookmarkedTours(tourRepository)

                        val tourViewModel = TourViewModel(
                            getToursUseCase,
                            searchToursUseCase,
                            bookmarkTourUseCase,
                            syncToursUseCase,
                            getBookmarkedToursUseCase
                        )
                        val authViewModel = AuthViewModel(userPreferences, tourApiService)
                        
                        // Create Imagen repository using ServiceLocator
                        val imagenRepository: ImagenRepository = ServiceLocator.getImagenRepository()
                        val profileViewModel = ProfileViewModel(userPreferences, tourApiService, imagenRepository, this)
                        
                        Triple(tourViewModel, authViewModel, profileViewModel)
                    }

                    val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()

                    LaunchedEffect(isLoggedIn) {
                        if (!isLoggedIn) {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        }
                    }
                    TourNavigation(navController, tourViewModel, authViewModel, profileViewModel)
                }
            }
        }
    }
}
