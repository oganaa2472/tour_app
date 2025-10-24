package com.example.survey.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import coil3.request.crossfade
import com.example.survey.R
import com.example.survey.ui.viewmodel.AuthViewModel
import com.example.survey.ui.viewmodel.AuthUiState
import com.example.survey.ui.viewmodel.UserInfo
import com.example.survey.ui.viewmodel.ProfileViewModel
import com.example.survey.ui.viewmodel.ProfileUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onBackClick: () -> Unit,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel
) {
    val context = LocalContext.current
    val authUiState by authViewModel.uiState.collectAsStateWithLifecycle()
    val profileUiState by profileViewModel.uiState.collectAsStateWithLifecycle()
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()

    var showAIPrompt by remember { mutableStateOf(false) }
    var aiPrompt by remember { mutableStateOf("") }

    // Handle photo update success
    LaunchedEffect(profileUiState.isPhotoUpdated) {
        if (profileUiState.isPhotoUpdated) {
            // Could trigger a refresh or toast message
        }
    }

    ProfileScreenContent(
        onLogout = onLogout,
        onBackClick = onBackClick,
        authUiState = authUiState,
        profileUiState = profileUiState,
        currentUser = currentUser,
        onGenerateAI = { prompt -> profileViewModel.generateAIImage(prompt) },
        onClearError = { profileViewModel.clearError() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    onLogout: () -> Unit,
    onBackClick: () -> Unit,
    authUiState: AuthUiState,
    profileUiState: ProfileUiState,
    currentUser: UserInfo?,
    onGenerateAI: (String) -> Unit,
    onClearError: () -> Unit
) {
    val context = LocalContext.current

    var showAIPrompt by remember { mutableStateOf(false) }
    var aiPrompt by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Photo Section
            Card(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val imageUrl = currentUser?.photo?.let {
                        "http://10.0.2.2:3000/img/users/$it"
                    }

                    // Show AI-generated image if available, otherwise show user photo
                    when {
                        profileUiState.generatedImage != null -> {
                            // Show the AI-generated image with indicator
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Image(
                                    bitmap = profileUiState.generatedImage!!.asImageBitmap(),
                                    contentDescription = "AI Generated Profile Photo",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
//                                    contentScale = ContentScale.Crop
                                )
                                
                                // AI indicator badge
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(24.dp)
                                        .background(
                                            MaterialTheme.colorScheme.secondary,
                                            CircleShape
                                        )
                                        .border(
                                            2.dp,
                                            MaterialTheme.colorScheme.surface,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.AutoAwesome,
                                        contentDescription = "AI Generated",
                                        modifier = Modifier.size(12.dp),
                                        tint = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            }
                        }
                        
                        imageUrl != null -> {
                            // Show current user photo from server
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Profile Photo",
                                modifier = Modifier
                                    .fillMaxSize()



                            )
                        }
                        
                        else -> {
                            // Show default avatar
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Default Avatar",
                                modifier = Modifier.size(60.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (profileUiState.generatedImage != null) {
                    "✨ AI-generated profile photo displayed!"
                } else {
                    "Generate your profile photo with AI"
                },
                style = MaterialTheme.typography.bodySmall,
                color = if (profileUiState.generatedImage != null) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // AI Generation Button
            Button(
                onClick = { showAIPrompt = true },
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Generate AI Profile Photo")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // User Information
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "User Information",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    currentUser?.let { user ->
                        ProfileInfoRow(
                            label = "Name",
                            value = user.name
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        ProfileInfoRow(
                            label = "Email",
                            value = user.email
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        ProfileInfoRow(
                            label = "User ID",
                            value = user.id
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Loading or Error States
            when {
                profileUiState.isLoading -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Updating photo...")
                        }
                    }
                }

                profileUiState.error != null -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Error,
                                    contentDescription = "Error",
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Error",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = profileUiState.error!!,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { onClearError() },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Dismiss")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Logout Button
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout")
            }
        }
    }

    // AI Prompt Dialog
    if (showAIPrompt) {
        AlertDialog(
            onDismissRequest = { showAIPrompt = false },
            title = { Text("Generate AI Profile Photo") },
            text = {
                Column {
                    Text("Describe the profile photo you want to generate:")
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = aiPrompt,
                        onValueChange = { aiPrompt = it },
                        label = { Text("Prompt") },
                        placeholder = { Text("e.g., Professional headshot, smiling person") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (aiPrompt.isNotBlank()) {
                            showAIPrompt = false
                            onGenerateAI(aiPrompt)
                        }
                    },
                    enabled = aiPrompt.isNotBlank()
                ) {
                    Text("Generate")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAIPrompt = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProfileInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}
