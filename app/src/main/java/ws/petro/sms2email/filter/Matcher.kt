package ws.petro.sms2email.filter

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import ws.petro.sms2email.await

class Matcher (context: Context) : LifecycleOwner {
    val context = context
    private val TAG: String = "Matcher"
    suspend fun match(messageFrom: String, messageText: String): Boolean {
        // Init database and read read all rules

        val ruleDao = RuleDatabase.getDatabaseSync(context).ruleDao()
        val repository = RuleRepository(ruleDao)

        val liveData = ruleDao.getAll()

//        GlobalScope.launch(Dispatchers.Main) {
//            Log.d(TAG, "Observe begin")
//            liveData.observeForever {
//                Log.d(TAG, "Reading async done, data size is ${it.size}")
//            }
//            Log.d(TAG, "Observe end")
//        }.join()

        liveData.await()

        val data = liveData.value!!

        Log.d(TAG, "Reading rules")

        for (item in data) {
            Log.d(TAG, "Found rule: ${item.title}")
        }

        Log.d(TAG, "Finish")

//        val nameObserver = Observer<List<Rule>> { items ->
//            for (item in items) {
//                Log.d(TAG, "Found rule: ${item.title}")
//            }
//        }

//        lifecycleScope.launch {
//            withContext(Dispatchers.Main) {
//                data.observe(this, nameObserver)
//            }
//        }



        return data.isNotEmpty() // todo
    }

    override fun getLifecycle(): Lifecycle {
        TODO("Not yet implemented")
    }
}
