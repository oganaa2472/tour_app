//package com.example.survey.ui.screen
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.example.survey.ui.viewmodel.AuthViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProfileScreen(
//    viewModel: AuthViewModel,
//    onBackClick: () -> Unit
//) {
//    val user by viewModel.user.collectAsStateWithLifecycle()
//    var username by remember { mutableStateOf(user?.username ?: "") }
//    var email by remember { mutableStateOf(user?.email ?: "") }
//
//    LaunchedEffect(user) {
//        user?.let {
//            username = it.username
//            email = it.email
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Update Profile") },
//                navigationIcon = {
//                    IconButton(onClick = onBackClick) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            OutlinedTextField(
//                value = username,
//                onValueChange = { username = it },
//                label = { Text("Username") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            OutlinedTextField(
//                value = email,
//                onValueChange = { email = it },
//                label = { Text("Email") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Button(
//                onClick = { viewModel.updateUser(username, email) },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Save")
//            }
//        }
//    }
//}
//package com.example.survey.ui.screen
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.example.survey.ui.viewmodel.AuthViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProfileScreen(
//    viewModel: AuthViewModel,
//    onBackClick: () -> Unit
//) {
//    val user by viewModel.user.collectAsStateWithLifecycle()
//    var username by remember { mutableStateOf(user?.username ?: "") }
//    var email by remember { mutableStateOf(user?.email ?: "") }
//
//    LaunchedEffect(user) {
//        user?.let {
//            username = it.username
//            email = it.email
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Update Profile") },
//                navigationIcon = {
//                    IconButton(onClick = onBackClick) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            OutlinedTextField(
//                value = username,
//                onValueChange = { username = it },
//                label = { Text("Username") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            OutlinedTextField(
//                value = email,
//                onValueChange = { email = it },
//                label = { Text("Email") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Button(
//                onClick = { viewModel.updateUser(username, email) },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Save")
//            }
//        }
//    }
//}
