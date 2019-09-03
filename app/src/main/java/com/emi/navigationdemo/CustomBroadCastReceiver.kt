package com.emi.navigationdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.emi.navigationdemo.MyFcmServiceSample.Companion.MESSAGE
import com.emi.navigationdemo.MyFcmServiceSample.Companion.TITLE


class CustomBroadCastReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {

            val title = intent?.getStringExtra(TITLE)
            val body = intent?.getStringExtra(MESSAGE)
            Log.d(CustomBroadCastReceiver::class.java.simpleName, "$title $body all data")
            val intentBroadcast = Intent(context, MainActivity::class.java)
            intentBroadcast.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intentBroadcast.putExtra(TITLE, title)
            intentBroadcast.putExtra(MESSAGE, body)
            context!!.startActivity(intentBroadcast)

    }

}