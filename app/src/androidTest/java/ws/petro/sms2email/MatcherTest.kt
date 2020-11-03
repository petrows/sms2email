package ws.petro.sms2email

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import ws.petro.sms2email.filter.Matcher

@RunWith(AndroidJUnit4::class)
class MatcherTest {
    @Test
    fun matchRule() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val matcher = Matcher(appContext)

        assertTrue(matcher.match("John Doe", "Hello world"))
    }
}
