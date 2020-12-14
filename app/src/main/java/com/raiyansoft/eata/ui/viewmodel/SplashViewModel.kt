package com.raiyansoft.eata.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.raiyansoft.eata.model.setting.Setting
import com.raiyansoft.eata.repository.ApiRepository
import com.raiyansoft.eata.util.Resource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "SplashViewModel"

    val liveData: LiveData<SplashState>
        get() = mutableLiveData
    private val mutableLiveData = MutableLiveData<SplashState>()

    init {
        GlobalScope.launch {
            delay(2000)
            mutableLiveData.postValue(SplashState.MainActivity)
        }
    }

    private val repository = ApiRepository()

    val dataStingLiveData = MutableLiveData<Resource<Setting>>()
    private var setting: Setting? = null


    private suspend fun getSetting(
        Authorization: String
    ) {
        dataStingLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getSetting(Authorization)
            dataStingLiveData.postValue(getSetting(response))
            Timber.d("$TAG getSetting-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataStingLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG getSetting-> Network Failure")
                }
                else -> {
                    dataStingLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG getSetting-> Conversion Error")
                }

            }
        }
    }

    private fun getSetting(response: Response<Setting>):
            Resource<Setting> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG getSetting->")
                setting = resultResponse
                Timber.d("$TAG getSetting->response->$setting")
                Timber.d("$TAG getSetting-> Resource.Success->$resultResponse")
                return Resource.Success(setting ?: resultResponse)
            }
        }
        Timber.e("$TAG getSetting->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    private fun getSetting() =
        viewModelScope.launch {
            getSetting(
                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHA6Ly9hdHRhMy5tZS9hcGkvdXNlci9yZWdpc3Rlci9uZWVkeSIsImlhdCI6MTU5ODc3OTA5NiwiZXhwIjoxNjA5MjkxMDk2LCJuYmYiOjE1OTg3NzkwOTYsImp0aSI6IkdiU0R2cGFDd1J2YXJlMjMifQ.AghkbX_jWYdTt3cKVHYvCcjQZTtme2sjdFu8To04Rro"
            )
        }

    init {
        getSetting()
    }

}

sealed class SplashState {
    object MainActivity : SplashState()
}
