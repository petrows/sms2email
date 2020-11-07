package ws.petro.sms2email

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions

class SimCard {
    var slotIndex: Int = 0
    var name: String = "?"
}

class SimInfo {
    private val TAG: String = "SimInfo"
    val info : String = ""

    fun getSimInfo(context: Context): List<SimCard>? {

        Log.d(TAG, "Gettim SIM card information")

        var simList = ArrayList<SimCard>()

        val subManager =
            context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        var subscriptionInfoList: List<SubscriptionInfo?>? = ArrayList()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        subscriptionInfoList = subManager.activeSubscriptionInfoList

        Log.d(TAG, subscriptionInfoList.toString())

        if (subscriptionInfoList == null) {
            Toast.makeText(context, "address not found", Toast.LENGTH_SHORT).show()
        }

        for (item in subscriptionInfoList) {
            Log.d(TAG, "Subscription: ${item}")

            var sim = SimCard()
            sim.slotIndex = item.simSlotIndex
            sim.name = item.displayName.toString()
            simList.add(sim)
        }

        return simList
    }

}
