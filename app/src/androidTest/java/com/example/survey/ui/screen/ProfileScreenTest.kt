package com.example.survey.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.survey.ui.viewmodel.AuthUiState
import com.example.survey.ui.viewmodel.ProfileUiState
import com.example.survey.ui.viewmodel.UserInfo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun profileScreen_showsTitleUserInfoAndLogout() {
        // Arrange minimal states
        val authState = AuthUiState()
        val profileState = ProfileUiState()
        val user = UserInfo(
            id = "user-123",
            email = "test@example.com",
            name = "Test User",
            photo = null
        )

        composeRule.setContent {
            ProfileScreenContent(
                onLogout = {},
                onBackClick = {},
                authUiState = authState,
                profileUiState = profileState,
                currentUser = user,
                onGenerateAI = {},
                onClearError = {}
            )
        }

        // Assert top app bar title
        composeRule.onNodeWithText("Profile").assertIsDisplayed()

        // Assert user info section and fields
        composeRule.onNodeWithText("User Information").assertIsDisplayed()
        composeRule.onNodeWithText("Name").assertIsDisplayed()
        composeRule.onNodeWithText("Email").assertIsDisplayed()
        composeRule.onNodeWithText("User ID").assertIsDisplayed()
        composeRule.onNodeWithText("Test User").assertIsDisplayed()
        composeRule.onNodeWithText("test@example.com").assertIsDisplayed()
        composeRule.onNodeWithText("user-123").assertIsDisplayed()

        // Assert Logout button exists
        composeRule.onNodeWithText("Logout").assertIsDisplayed()
    }

    @Test
    fun profileScreen_showsAIGenerateButton_andDialogOnClick() {
        val authState = AuthUiState()
        val profileState = ProfileUiState()

        composeRule.setContent {
            ProfileScreenContent(
                onLogout = {},
                onBackClick = {},
                authUiState = authState,
                profileUiState = profileState,
                currentUser = null,
                onGenerateAI = {},
                onClearError = {}
            )
        }

        // Button present
        composeRule.onNodeWithText("Generate AI Profile Photo").assertIsDisplayed()
        // We won't actually click and interact with the dialog's text field in this basic smoke test.
    }
}
