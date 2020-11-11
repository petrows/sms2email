package ws.petro.sms2email

import android.R
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.github.tntkhang.gmailsenderlibrary.GMailSender
import com.github.tntkhang.gmailsenderlibrary.GmailListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ws.petro.sms2email.filter.Matcher
import android.preference.PreferenceManager;

class SMSReceiver : BroadcastReceiver() {
    private val TAG: String = "SMSReceiver"
    override fun onReceive(context: Context, intent: Intent) {

        var matcher = Matcher(context)

        var prefs = PreferenceManager.getDefaultSharedPreferences(context);

        // Get SMS message
        val data = intent.extras
        val pdusObj = data!!.get("pdus") as Array<*>

        for (i in pdusObj.indices) {
            val currentMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                Telephony.Sms.Intents.getMessagesFromIntent(intent)[0]
            else SmsMessage.createFromPdu(pdusObj[0] as ByteArray)

            val phoneNumber = currentMessage.displayOriginatingAddress
            Log.i("receiver", phoneNumber)

            var simSlotIndex: Int = data.getInt("slot", -1)

            val sub: Int = data.getInt("subscription", -1)

            if (simSlotIndex == -1) {
                simSlotIndex = sub - 1
            }

            Log.i(TAG, "SMS: $phoneNumber, subscription: $sub, slot: $simSlotIndex")

            val smsContent = currentMessage.getDisplayMessageBody()
            Log.d("receiver", "Message: " + smsContent) // Always max 67 characters!

            GlobalScope.launch(Dispatchers.IO) {
                val rules = matcher.match(phoneNumber, smsContent, simSlotIndex)

                if (rules.isEmpty()) {
                    // Nothing to do
                    return@launch
                }

                // Process rules and send emails
                var emails : ArrayList<String> = ArrayList<String>()
                for (rule in rules) {
                    emails.add(rule.emailTo)
                }
                for (email in emails.distinct()) {
                    Log.d(TAG, "Send message to: $email")

                    GMailSender.withAccount(prefs.getString("smtp_user", ""), prefs.getString("smtp_password", ""))
                        .withTitle("SMS on slot $simSlotIndex")
                        .withBody("$smsContent")
                        .withSender(prefs.getString("smtp_user", ""))
                        .toEmailAddress(email) // one or multiple addresses separated by a comma
                        .withListenner(object : GmailListener {
                            override fun sendSuccess() {
                                Log.d(TAG, "Message sent")
                            }
                            override fun sendFail(err: String) {
                                Log.d(TAG, "Message send failed")
                            }
                        })
                        .send()
                }
            }
        }
    }
}
