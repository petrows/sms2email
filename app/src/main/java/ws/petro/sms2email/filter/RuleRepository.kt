package ws.petro.sms2email.filter

/*
    Based on example: https://github.com/googlecodelabs/android-room-with-a-view/tree/kotlin
 */

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */
class RuleRepository(private val ruleDao: RuleDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allRules: LiveData<List<Rule>> = ruleDao.getAll()

    // You must call this on a non-UI thread or your app will crash. So we're making this a
    // suspend function so the caller methods know this.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(rule: Rule) {
        ruleDao.save(rule)
    }
}
