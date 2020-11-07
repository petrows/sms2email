package ws.petro.sms2email

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ws.petro.sms2email.filter.Matcher

class SMSReceiver : BroadcastReceiver() {
    private val TAG: String = "SMSReceiver"
    override fun onReceive(context: Context, intent: Intent) {

        var matcher = Matcher(context)

        // Get SMS message
        val data = intent.extras
        val pdusObj = data!!.get("pdus") as Array<*>

        for (i in pdusObj.indices) {
            val currentMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                Telephony.Sms.Intents.getMessagesFromIntent(intent)[0]
            else SmsMessage.createFromPdu(pdusObj[0] as ByteArray)

            val phoneNumber = currentMessage.displayOriginatingAddress
            Log.e("receiver", phoneNumber)

            val smsIndex: Int = data.getInt("slot", -1)

            val sub: Int = data.getInt("subscription", -1)

            val senderNum = phoneNumber
            Log.e("receiver", senderNum)

            val smsContent = currentMessage.getDisplayMessageBody()
            Log.d("receiver", "Message: " + smsContent) // Always max 67 characters!

            GlobalScope.launch(Dispatchers.IO) {
                val rules = matcher.match(senderNum, smsContent, sub)

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
                }
            }
        }
    }
}
