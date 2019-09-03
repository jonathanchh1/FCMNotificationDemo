package com.emi.navigationdemo

import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.squareup.okhttp.*
import java.io.IOException

class FcmSender{

    val JSON = MediaType.parse("application/json; charset=utf-8")
    val url = "https://fcm.googleapis.com/fcm/send"
    val serverkey = "enter your server key"
   // val url = "https://fcm.googleapis.com/v1/demo/com.emi.navigationdemo/messages:send"


    private val gson : Gson = Gson()
    private val OkHttpClient : OkHttpClient = OkHttpClient()

    fun sendData(data : Alarm){
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
                if(it.isSuccessful){
                    val dataFormat = PushData()
                    dataFormat.data = data
                    dataFormat.to = it.result?.token
                    val body = RequestBody.create(JSON, gson.toJson(dataFormat))
                    val request = Request
                            .Builder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "key=$serverkey")
                            .url(url)
                            .post(body)
                            .build()

                    OkHttpClient.newCall(request).enqueue(object : Callback{

                        override fun onResponse(response: Response?) {
                                println(response?.body()!!.string())
                            }

                            override fun onFailure(request: Request?, e: IOException?) {
                                println("${e!!.printStackTrace()}")
                            }
                        })
                    }
                }
            }

}