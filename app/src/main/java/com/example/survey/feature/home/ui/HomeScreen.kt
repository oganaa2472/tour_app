
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.survey.navigation.BottomNavItem

@Composable
fun HomeScreen(
    onLogout: () -> Unit
) {
    var selectedItem by remember { mutableStateOf(0) }

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Bookings,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (items[selectedItem]) {
                BottomNavItem.Home -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Welcome to Natours Home!",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // Example tour list placeholder
                        Text("Here you can browse tours")
                    }
                }
                BottomNavItem.Bookings -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Your Bookings",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("You can see all your booked tours here")
                    }
                }
                BottomNavItem.Profile -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Profile Screen",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onLogout) {
                            Text("Logout")
                        }
                    }
                }
            }
        }
    }
}
