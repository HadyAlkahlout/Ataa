package com.raiyansoft.eata.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.eata.model.privacy.Privacy
import com.raiyansoft.eata.repository.ApiRepository
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Constants.TOKEN
import com.raiyansoft.eata.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class PrivacyViewModel(application: Application) :
    AndroidViewModel(application) {

    private val TAG = "PrivacyViewModel"

    private val repository = ApiRepository()

    val dataPrivacyLiveData = MutableLiveData<Resource<Privacy>>()
    private var privacy: Privacy? = null
    private val share = Constants.getSharePref(application.applicationContext)


    private suspend fun getPrivacy(
        Authorization: String
    ) {
        dataPrivacyLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getPrivacy(Authorization)
            dataPrivacyLiveData.postValue(getPrivacy(response))
            Timber.d("$TAG getPrivacy-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataPrivacyLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG getPrivacy-> Network Failure")
                }
                else -> {
                    dataPrivacyLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG getPrivacy-> Conversion Error")
                }

            }
        }
    }

    private fun getPrivacy(response: Response<Privacy>):
            Resource<Privacy> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG getPrivacy->")
                privacy = resultResponse
                Timber.d("$TAG getPrivacy->response->$privacy")
                Timber.d("$TAG getPrivacy-> Resource.Success->$resultResponse")
                return Resource.Success(privacy ?: resultResponse)
            }
        }
        Timber.e("$TAG getPrivacy->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    private fun getPrivacy() =
        viewModelScope.launch {
            getPrivacy(
                share.getString(TOKEN, "")!!
            )
        }

    init {
        getPrivacy()
    }

}