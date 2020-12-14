package com.raiyansoft.eata.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.eata.model.about.AboutUs
import com.raiyansoft.eata.repository.ApiRepository
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class AboutUsViewModel(application: Application) :
    AndroidViewModel(application) {

    private val TAG = "aboutViewModel"

    private val repository = ApiRepository()

    val dataAboutUsLiveData = MutableLiveData<Resource<AboutUs>>()
    private var about: AboutUs? = null
    private val share = Constants.getSharePref(application.applicationContext)


    private suspend fun getAboutUs(
        Authorization: String
    ) {
        dataAboutUsLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getAbout(Authorization)
            dataAboutUsLiveData.postValue(getaboutUs(response))
            Timber.d("$TAG getPrivacy-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataAboutUsLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG getPrivacy-> Network Failure")
                }
                else -> {
                    dataAboutUsLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG getPrivacy-> Conversion Error")
                }

            }
        }
    }

    private fun getaboutUs(response: Response<AboutUs>):
            Resource<AboutUs> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG getAboutUs->")
                about = resultResponse
                Timber.d("$TAG getAboutUs->response->$about")
                Timber.d("$TAG getAboutUs-> Resource.Success->$resultResponse")
                return Resource.Success(about ?: resultResponse)
            }
        }
        Timber.e("$TAG getAboutUs->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    private fun getAboutUs() =
        viewModelScope.launch {
            getAboutUs(
                share.getString(Constants.TOKEN, "")!!
            )
        }

    init {
        getAboutUs()
    }

}