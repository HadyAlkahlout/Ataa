package com.raiyansoft.eata.message

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

data class PushNotification (val data: NotificationData?, val to:String){
    constructor():this(null,"")

    inner class Notification{
         fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if(response.isSuccessful) {
                    Timber.e("eee send Response: Notification sended Successfuly")
                } else {
                    Timber.e("eee send Response: Notification ${response.errorBody().toString()}")
                }
            } catch(e: Exception) {
                Timber.e("eee send Response: Notification ${e.message.toString()}")
            }
        }

    }
}