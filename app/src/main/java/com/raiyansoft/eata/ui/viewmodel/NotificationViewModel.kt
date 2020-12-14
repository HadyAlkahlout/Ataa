package com.raiyansoft.eata.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.eata.model.notification.Notification
import com.raiyansoft.eata.repository.ApiRepository
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class NotificationViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "NotificationViewModel"

    private val repository = ApiRepository()

    val dataNotificationLiveData = MutableLiveData<Resource<Notification>>()
    private var notification: Notification? = null
    private var page = 1
    private val share = Constants.getSharePref(application.applicationContext)


    private suspend fun getNotification(
        Authorization: String
    ) {
        dataNotificationLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getNotification(Authorization, page.toString())

            dataNotificationLiveData.postValue(getNotification(response))
            Timber.d("$TAG getNotification-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataNotificationLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG getNotification-> Network Failure")
                }
                else -> {
                    dataNotificationLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG getNotification-> Conversion Error")
                }

            }
        }
    }

    private fun getNotification(response: Response<Notification>):
            Resource<Notification> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                page++
                Timber.d("$TAG getNotification->")
                if (notification == null) {
                    notification = resultResponse
                    Timber.d("$TAG getNotification->response->$notification")
                } else {
                    val oldNotification = notification
                    oldNotification!!.data.data.addAll(resultResponse.data.data)
                }
                Timber.d("$TAG getNotification-> Resource.Success->$resultResponse")
                return Resource.Success(notification ?: resultResponse)

            }
        }
        Timber.e("$TAG getNotification->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


     fun getNotification() =
        viewModelScope.launch {
            getNotification(
                share.getString(Constants.TOKEN, "")!!            )
        }



}