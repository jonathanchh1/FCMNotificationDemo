package com.emi.navigationdemo



import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.emi.navigationdemo.MyFcmServiceSample.Companion.MESSAGE
import com.emi.navigationdemo.MyFcmServiceSample.Companion.TITLE
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.rpc.LocalizedMessage
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){

    private lateinit var notificationHelper : NotificationHelper
    private val notificationId : Int = 1102
    private val fcmSender = FcmSender()
    private val receiver = CustomBroadCastReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notificationHelper = NotificationHelper(this)
        FirebaseFirestore.getInstance()
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(MESSAGE))

        val title = intent.getStringExtra(TITLE)
        val message = intent.getStringExtra(MESSAGE)

        subscribeButton.setOnClickListener {
            Log.d(MainActivity::class.java.simpleName, "Subscribing")
            FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener{
                    task ->
                    var msg = getString(R.string.msg_subscribe)
                    if(!task.isSuccessful){
                        msg = getString(R.string.msg_failed)
                    }
                    Log.d(MainActivity::class.java.simpleName, "$msg")
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                }
        }

        logTokenButton.setOnClickListener {
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener {
                    task ->
                    if(!task.isSuccessful){
                        Log.w(MainActivity::class.java.simpleName, "${task.exception}")
                        return@addOnCompleteListener
                    }

                    val token = task.result?.token
                    val msg = getString(R.string.msg_token_fmt)
                    Log.d(MainActivity::class.java.simpleName, "token id $token  $msg")
                    Toast.makeText(applicationContext, token, Toast.LENGTH_SHORT).show()
                }
        }

        sendMessage.setOnClickListener {
                sendNotification(notificationId, title, message)

        }

    }


    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    private fun sendNotification(id : Int, title : String?, message : String?){
        val alarms = Alarm()
        alarms.id = id.toString()
        alarms.userName = notificationHelper.randomNames
        alarms.userEmail = "example@gmail.com"
        alarms.timestamp = System.currentTimeMillis()
        alarms.title = getString(R.string.follower_title_notification)
        alarms.message = getString(R.string.following, alarms.userName)
        FirebaseFirestore.getInstance().collection("channel").add(alarms)
            .addOnSuccessListener {
                    Log.d(MainActivity::class.java.simpleName, "${it.id} its id")
            }

            notificationHelper.notify(alarms.id.toInt(),
            notificationHelper.getNotificationFollower(title, message,this))

        fcmSender.sendData(alarms)
    }



}
