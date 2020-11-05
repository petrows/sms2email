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


class SimInfo {
    private val TAG: String = "SimInfo"
    val info : String = ""

    fun getSimInfo(context: Context): List<SubscriptionInfo?>? {

        Log.d(TAG, "Gettim SIM card information")

        val subManager =
            context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        var subscriptionInfoList: List<SubscriptionInfo?>? = ArrayList()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.READ_PHONE_STATE), 0)
            return null
        }
        subscriptionInfoList = subManager.activeSubscriptionInfoList
        Log.d(TAG, subscriptionInfoList.toString())
        if (subscriptionInfoList == null) {
            Toast.makeText(context, "address not found", Toast.LENGTH_SHORT).show()
        }

        for (item in subscriptionInfoList) {
            Log.d(TAG, "Subscription: ${item}")
        }

        return subscriptionInfoList
    }

}
