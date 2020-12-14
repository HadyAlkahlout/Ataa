package com.raiyansoft.eata.message

import com.raiyansoft.eata.util.Constants.Content_type
import com.raiyansoft.eata.util.Constants.ServerKey
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @Headers("Authorization:key=$ServerKey","Content-Type:$Content_type")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ):Response<ResponseBody>
}