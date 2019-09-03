package com.emi.navigationdemo
import android.app.*
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import java.util.*


class NotificationHelper(val context: Context) : ContextWrapper(context){


    companion object{
        val FOLLOWERS_CHANNEL = "follower"
    }


    private val subscribeChannel : NotificationChannel
    private val fcmService = MyFcmServiceSample()
    private val mNotificationManager : NotificationManager by lazy{
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }


    init {
        subscribeChannel = NotificationChannel(
            FOLLOWERS_CHANNEL,
            context.getString(R.string.notification_channel_names),
            NotificationManager.IMPORTANCE_DEFAULT
        )


    }


    fun getNotificationFollower(title : String?, message : String?, context: Context): NotificationCompat.Builder{
        subscribeChannel.description = "subscriber channel"
        subscribeChannel.enableLights(true)
        subscribeChannel.lightColor = Color.RED
        subscribeChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
        subscribeChannel.enableVibration(true)
        mNotificationManager.createNotificationChannel(subscribeChannel)
        return fcmService.sendNotification(title, message, context, pendingIntent)
    }


   val pendingIntent : PendingIntent
        get() {
            val openMainIntent = Intent(this, MainActivity::class.java)
            val stackBuilder = TaskStackBuilder.create(this)
            stackBuilder.addParentStack(MainActivity::class.java)
            stackBuilder.addNextIntent(openMainIntent)
            return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT)
        }



    fun notify(id : Int, notification: NotificationCompat.Builder){
        mNotificationManager.notify(id, notification.build())
    }


    val randomNames : String
    get(){
        val names = applicationContext.resources.getStringArray(R.array.names_array)
        return names[Random().nextInt(names.size)]
    }
}