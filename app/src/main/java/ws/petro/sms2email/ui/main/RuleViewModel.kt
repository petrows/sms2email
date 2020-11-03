package ws.petro.sms2email.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ws.petro.sms2email.filter.Rule
import ws.petro.sms2email.filter.RuleDatabase
import ws.petro.sms2email.filter.RuleRepository

class RuleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RuleRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allRules: LiveData<List<Rule>>

    init {
        val ruleDao = RuleDatabase.getDatabase(application, viewModelScope).ruleDao()
        repository = RuleRepository(ruleDao)
        allRules = repository.allRules
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(rule: Rule) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(rule)
    }
}
