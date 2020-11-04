package ws.petro.sms2email.filter

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import ws.petro.sms2email.await

class Matcher (context: Context) {
    val context = context
    private val TAG: String = "Matcher"
    suspend fun match(messageFrom: String, messageText: String, simId: Int): Boolean {
        // Init database and read read all rules

        val ruleDao = RuleDatabase.getDatabaseSync(context).ruleDao()
        val repository = RuleRepository(ruleDao)

        val liveData = ruleDao.getAll()

        liveData.await()

        val data = liveData.value

        if (data == null) {
            Log.e(TAG, "Error getting rules list for filtering")
            return false
        }

        

        Log.d(TAG, "Reading rules")

        for (item in data) {
            Log.d(TAG, "Found rule: ${item.title}")
        }

        Log.d(TAG, "Finish")

        return data.isNotEmpty() // todo
    }
}
