package ws.petro.sms2email.ui.main

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import ws.petro.sms2email.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}
