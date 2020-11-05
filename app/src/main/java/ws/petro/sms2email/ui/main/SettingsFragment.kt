package ws.petro.sms2email.ui.main

import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ws.petro.sms2email.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        /**
         * Set numeric setting type
         * https://developer.android.com/guide/topics/ui/settings/customize-your-settings#customize_an_edittextpreference_dialog
         */
        val portPreference: EditTextPreference? = findPreference("smtp_port")
        portPreference?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }

        /**
         * Set password option type
         */
        val passwordPreference: EditTextPreference? = findPreference("smtp_password")
        passwordPreference?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        /**
         * Set custom display to hide our password
         * https://developer.android.com/guide/topics/ui/settings/customize-your-settings#use_a_custom_summaryprovider
         */
        passwordPreference?.summaryProvider =
            Preference.SummaryProvider<EditTextPreference> { preference ->
                val text = preference.text
                if (TextUtils.isEmpty(text)) {
                    "Not set"
                } else {
                    var retString : String = ""
                    for (x in text.indices) {
                        retString += "*"
                    }
                    retString
                }
            }
    }
}
