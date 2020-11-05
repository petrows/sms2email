package ws.petro.sms2email

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            GlobalScope.launch(Dispatchers.IO) {
                ruleDao.deleteAll()
                ruleDao.insert(Rule("test-1", 3, -1, "test1@example.com"))
                ruleDao.insert(Rule("test-2", 2, -1, "test2@example.com"))
                ruleDao.insert(Rule("test-3", 1, -1, "test3@example.com"))

                matcher.match("John Doe", "Hello world", 1) { rules ->
                    assertTrue(rules.isNotEmpty())
                    assertEquals(rules[0].title, "test-3")
                }
            }
        }
    }

    @Test
    fun matchRuleSim() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            GlobalScope.launch(Dispatchers.IO) {
                ruleDao.deleteAll()
                ruleDao.insert(Rule("test-1", 3, 3, "test1@example.com"))
                ruleDao.insert(Rule("test-2", 2, 2, "test2@example.com"))
                ruleDao.insert(Rule("test-3", 1, -1, "test3@example.com"))

                matcher.match("John Doe", "Hello world", 2) { rules ->
                    assertFalse(rules.isNotEmpty())
                    assertEquals(rules[0].title, "test-3")
                    assertEquals(rules[1].title, "test-2")
                }
            }
        }
    }
}
