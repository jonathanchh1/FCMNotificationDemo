package com.emi.navigationdemo


import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.emi.navigationdemo.NotificationHelper.Companion.FOLLOWERS_CHANNEL
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFcmServiceSample  : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(MyFcmServiceSample::class.java.simpleName,"Message data payLoad" + remoteMessage.data)

        if(true){
            scheduleJob()
        }else{
            handleNow()
        }

        val title = remoteMessage.data.getValue("title")
        val body = remoteMessage.data.getValue("message")

        val intentNoti = Intent(MESSAGE)
          intentNoti.action = MESSAGE
         intentNoti.putExtra(TITLE, title)
         intentNoti.putExtra(MESSAGE, body)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentNoti)
   }

    override fun onNewToken(token: String) {
        Log.d(MyFcmServiceSample::class.java.simpleName, "Refreshed Token: $token")
        sendRegistrationToServer(token)
    }


    private fun scheduleJob(){
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
        WorkManager.getInstance().beginWith(work).enqueue()
    }



    private fun sendRegistrationToServer(token : String) {
        Log.d(MyFcmServiceSample::class.java.simpleName, "$token")

    }


    private fun handleNow() {
        Log.d(MyFcmServiceSample::class.java.simpleName, "perform short time task")
    }


    fun sendNotification(
        title: String?,
        message: String?, context: Context, pendingIntent: PendingIntent?): NotificationCompat.Builder {
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            return NotificationCompat.Builder(context, FOLLOWERS_CHANNEL)
                .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(message)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        }

    companion object{

        const val TITLE = "title"
        const val MESSAGE = "message"
    }
}
