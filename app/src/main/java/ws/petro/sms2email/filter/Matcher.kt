package ws.petro.sms2email.filter

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import ws.petro.sms2email.await
import java.lang.RuntimeException

class Matcher (context: Context) {
    val context = context
    private val TAG: String = "Matcher"
    suspend fun match(messageFrom: String, messageText: String, simId: Int): List<Rule> {
        // Init database and read read all rules

        val ruleDao = RuleDatabase.getDatabaseSync(context).ruleDao()
        val repository = RuleRepository(ruleDao)

        val liveData = ruleDao.getAll()
        var matchedRules : ArrayList<Rule> = ArrayList<Rule>()

        liveData.await()

        val data = liveData.value

        if (data == null) {
            Log.e(TAG, "Error getting rules list for filtering")
            throw RuntimeException("Error getting rules list for filtering")
        }

        Log.d(TAG, "Reading rules")

        for (rule in data) {
            Log.d(TAG, "Matching rule: ${rule.title}")

            // Sim?
            if (rule.sim != -1 && rule.sim != simId) {
                // Mismatch
                continue
            }

            // Match?
            matchedRules.add(rule)
        }

        Log.d(TAG, "Matched ${matchedRules.size} rules")

        return matchedRules
    }
}
