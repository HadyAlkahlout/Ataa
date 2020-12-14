package com.raiyansoft.eata.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.eata.model.profile.Profile
import com.raiyansoft.eata.repository.ApiRepository
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Constants.TOKEN
import com.raiyansoft.eata.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class ProfileViewModel(application: Application) : AndroidViewModel(application) {


    private val TAG = "ProfileViewModel"

    private val repository = ApiRepository()

    val dataProfileLiveData = MutableLiveData<Resource<Profile>>()
    private var profile: Profile? = null
    private val share = Constants.getSharePref(application.applicationContext)


    private suspend fun getProfile(
        Authorization: String
    ) {
        dataProfileLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getProfile(Authorization)
            dataProfileLiveData.postValue(getProfile(response))
            Timber.d("$TAG getProfile-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataProfileLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG getProfile-> Network Failure")
                }
                else -> {
                    dataProfileLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG getProfile-> Conversion Error")
                }

            }
        }
    }

    private fun getProfile(response: Response<Profile>):
            Resource<Profile> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG getProfile->")
                profile = resultResponse
                Timber.d("$TAG getProfile->response->$profile")
                Timber.d("$TAG getProfile-> Resource.Success->$resultResponse")
                return Resource.Success(profile ?: resultResponse)
            }
        }
        Timber.e("$TAG getProfile->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    private fun getProfile() =
        viewModelScope.launch {
            getProfile(
                share.getString(TOKEN, "")!!
            )
        }

    init {
        getProfile()
    }

}