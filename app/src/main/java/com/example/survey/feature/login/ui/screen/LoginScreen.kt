package com.example.survey.feature.login.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.survey.R
import com.example.survey.feature.login.data.local.TokenManager
import com.example.survey.feature.login.data.remote.service.UserService
import com.example.survey.feature.login.data.repository.LoginRepositoryImpl
import com.example.survey.feature.login.ui.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    // KTS: Suggestion: Pass navigation actions as parameters
    // for better testability and flow control.
     onLoginSuccess: () -> Unit,
    // onNavigateToSignUp: () -> Unit
) {

    // KTS: Suggestion: This is functional but not ideal for production.
    // The ViewModel should be provided by a dependency injection
    // framework like Hilt (e.g., @HiltViewModel) or passed down
    // from a navigation graph-scoped factory.
    val userService: UserService = remember { ApiProvider.userService }
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val loginViewModel: LoginViewModel = viewModel {
        LoginViewModel(LoginRepositoryImpl(userService),tokenManager)
    }
    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
    var passwordVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    // --- UX Flow Improvement ---
    // Handle the success state as a one-time event to navigate away.
    // Showing a "success" message on the login screen is poor UX.
    // The goal is to log in and *proceed*.
    LaunchedEffect(uiState.success) {
        if (uiState.success) {
             onLoginSuccess() // TODO: Trigger navigation to the home screen
            // For now, we'll just clear the focus
            focusManager.clearFocus()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // --- Layout Improvement ---
        // 1. Use verticalScroll to prevent UI from crushing when keyboard appears.
        // 2. Use Arrangement.SpaceBetween to push footer to the bottom.
        // 3. Use column weights to center the main content block.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // This spacer pushes the content block towards the center
            Spacer(modifier = Modifier.weight(1f))

            // --- Main Content Block ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // 🌿 Logo
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Natours Logo",
                    modifier = Modifier.size(100.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ✨ Title
                Text(
                    text = "Welcome back to Natours!",
                    // KTS: Use built-in type scale for consistency
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 📧 Email
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { loginViewModel.onEmailChange(it) },
                    label = { Text("Email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next // UX: Go to next field
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    // --- State Handling Improvement ---
                    // Disable fields when loading
                    enabled = !uiState.isLoading,
                    // KTS: Clear error when user starts typing again
                    // (This logic is best placed in the ViewModel's onEmailChange)
                    isError = uiState.errorMessage != null
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 🔒 Password
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { loginViewModel.onPasswordChange(it) },
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done // UX: Submit form
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (!uiState.isLoading) {
                                loginViewModel.login()
                            }
                        }
                    ),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                        val description = if (passwordVisible) "Hide password" else "Show password"
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = description)
                        }
                    },
                    // --- State Handling Improvement ---
                    enabled = !uiState.isLoading,
                    isError = uiState.errorMessage != null
                )

                // 🧩 Error message
                // Placed *before* the button for better visual flow
                if (uiState.errorMessage != null) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = uiState.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 🔘 Login Button
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        loginViewModel.login()
                    },
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = MaterialTheme.shapes.medium
                    // KTS: No need to explicitly set containerColor
                    // if it's meant to be primary (it's the default).
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 3.dp
                        )
                    } else {
                        Text("Log in", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            // This spacer pushes the footer to the bottom
            Spacer(modifier = Modifier.weight(1.5f))

            // --- Footer Improvement ---
            // Make the "Sign up" text interactive
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Don’t have an account?",
                    style = MaterialTheme.typography.bodyMedium
                )
                TextButton(
                    onClick = { /* onNavigateToSignUp() */ } // TODO: Navigate to sign-up
                ) {
                    Text("Sign up now")
                }
            }
        }
    }
}