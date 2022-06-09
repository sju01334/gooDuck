package com.nepplus.gooduck.fcm

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFCM : FirebaseMessagingService() {

    private val TAG = "MessagingService"

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title =message.notification!!.title

        message.notification?.let {
            Log.d(TAG, "Notification Body : ${it.body}")
        }

        message.data.isNotEmpty().let{
            Log.d(TAG, "Message Data : ${message.data}")
            val title = message.data["title"]
        }

//        UI 쓰레드에 토스트를 띄우는 업무를 부여
//        UI 쓰레드에 일을 맡겨주는 Handler
        val myHandler = Handler(Looper.getMainLooper())

        myHandler.post {
            Toast.makeText(applicationContext, title, Toast.LENGTH_SHORT).show()
        }


    }
}