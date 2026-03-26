package io.github.rebeccacalabretta.teamops

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import io.github.rebeccacalabretta.teamops.ui.punch.PunchBottomSection
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PunchScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun punchButton_triggersCallback() {
        var clicked = false

        composeTestRule.setContent {
            PunchBottomSection(
                statusText = "Checked out",
                buttonText = "Check In",
                isProcessing = false,
                onButtonClick = { clicked = true }
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("punchButton")
            .performClick()

        assertTrue("Callback should have been triggered", clicked)
    }
}