package ws.petro.sms2email

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.runBlocking
import ws.petro.sms2email.filter.Matcher

class SMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        var matcher = Matcher(context)

        runBlocking {
            matcher.match("as", "sa", 1)
        }
    }
}
