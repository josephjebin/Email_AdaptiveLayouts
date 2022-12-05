package com.example.reply.test

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.reply.R.string.navigation_back
import com.example.reply.R.string.reply_details
import com.example.reply.data.local.LocalEmailsDataProvider
import com.example.reply.ui.ReplyApp
import org.junit.Rule
import org.junit.Test

class ReplyAppStateRestorationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    @TestCompactWidth
    fun compactDevice_selectedEmailRetained_afterConfigChange() {
        val stateRestorationTester = StateRestorationTester(composeTestRule)
        stateRestorationTester.setContent {
            ReplyApp(windowSize = WindowWidthSizeClass.Compact)
        }

        composeTestRule.onNodeWithText(LocalEmailsDataProvider.allEmails[2].body)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(LocalEmailsDataProvider.allEmails[2].subject)
            .performClick()

        //back button is present, so the app is displaying the details screen
        composeTestRule.onNodeWithContentDescriptionForStringId(navigation_back).assertIsDisplayed()
        composeTestRule.onNodeWithText(LocalEmailsDataProvider.allEmails[2].body).assertExists()

        stateRestorationTester.emulateSavedInstanceStateRestore()

        //back button is present, so the app is displaying the details screen
        composeTestRule.onNodeWithContentDescriptionForStringId(navigation_back).assertIsDisplayed()
        composeTestRule.onNodeWithText(LocalEmailsDataProvider.allEmails[2].body).assertExists()
    }

    @Test
    @TestExpandedWidth
    fun expandedDevice_selectedEmailEmailRetained_afterConfigChange() {
        val stateRestorationTester = StateRestorationTester(composeTestRule)
        stateRestorationTester.setContent {
            ReplyApp(windowSize = WindowWidthSizeClass.Expanded)
        }

        // Given third email is displayed
        composeTestRule.onNodeWithText(LocalEmailsDataProvider.allEmails[2].body)
            .assertIsDisplayed()

        // Select third email
        composeTestRule.onNodeWithText(LocalEmailsDataProvider.allEmails[2].subject)
            .performClick()

        // Verify the details component displays the details of the email
        composeTestRule.onNodeWithTagForStringId(reply_details).onChildren()
            .assertAny(hasAnyDescendant(hasText(LocalEmailsDataProvider.allEmails[2].body)))

        // Simulate a config change
        stateRestorationTester.emulateSavedInstanceStateRestore()

        // Verify the details component displays the details of the email
        composeTestRule.onNodeWithTagForStringId(reply_details).onChildren()
            .assertAny(hasAnyDescendant(hasText(LocalEmailsDataProvider.allEmails[2].body)))
    }
}