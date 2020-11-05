package ws.petro.sms2email

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import ws.petro.sms2email.filter.Matcher
import ws.petro.sms2email.filter.Rule
import ws.petro.sms2email.filter.RuleDatabase

@RunWith(AndroidJUnit4::class)
class MatcherTest {
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    val ruleDao = RuleDatabase.getDatabaseSync(appContext).ruleDao()
    val matcher = Matcher(appContext)

    @Test
    fun matchRuleOrder() {
        runBlocking {
            ruleDao.deleteAll()
            ruleDao.insert(Rule("test-1", 3, -1, "test1@example.com"))
            ruleDao.insert(Rule("test-2", 2, -1, "test2@example.com"))
            ruleDao.insert(Rule("test-3", 1, -1, "test3@example.com"))
            val matches = matcher.match("John Doe", "Hello world", 1)

            assertTrue(matches.isNotEmpty())
            assertEquals(matches[0].title, "test-3")
        }
    }

    @Test
    fun matchRuleSim() {
        runBlocking {
            ruleDao.deleteAll()
            ruleDao.insert(Rule("test-1", 3, 3, "test1@example.com"))
            ruleDao.insert(Rule("test-2", 2, 2, "test2@example.com"))
            ruleDao.insert(Rule("test-3", 1, -1, "test3@example.com"))

            val matches = matcher.match("John Doe", "Hello world", 2)

            assertTrue(matches.isNotEmpty())
            assertEquals(matches[0].title, "test-3")
            assertEquals(matches[1].title, "test-2")
        }
    }
}
