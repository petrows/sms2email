package ws.petro.sms2email.filter

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Matcher (context: Context) : LifecycleOwner {
    val context = context
    private val TAG: String = "Matcher"
    fun match(messageFrom: String, messageText: String): Boolean {
        // Init database and read read all rules

        val ruleDao = RuleDatabase.getDatabaseSync(context).ruleDao()
        val repository = RuleRepository(ruleDao)

        val data = ruleDao.getAll()

        val nameObserver = Observer<List<Rule>> { items ->
            for (item in items) {
                Log.d(TAG, "Found rule: ${item.title}")
            }
        }

//        lifecycleScope.launch {
//            withContext(Dispatchers.Main) {
//                data.observe(this, nameObserver)
//            }
//        }



        return false // todo
    }

    override fun getLifecycle(): Lifecycle {
        TODO("Not yet implemented")
    }
}
