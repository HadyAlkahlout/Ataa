package com.raiyansoft.eata.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.eata.model.conditions.Conditions
import com.raiyansoft.eata.repository.ApiRepository
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Constants.TOKEN
import com.raiyansoft.eata.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException


class ConditionsViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "ConditionsViewModel"

    private val repository = ApiRepository()

    val dataConditionsLiveData = MutableLiveData<Resource<Conditions>>()
    private var conditions: Conditions? = null
    private val share = Constants.getSharePref(application.applicationContext)


    private suspend fun getConditions(
        Authorization: String
    ) {
        dataConditionsLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getConditions(Authorization)

            dataConditionsLiveData.postValue(getConditions(response))
            Timber.d("$TAG getAllCategories-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataConditionsLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG getAllCategories-> Network Failure")
                }
                else -> {
                    dataConditionsLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG getAllCategories-> Conversion Error")
                }

            }
        }
    }

    private fun getConditions(response: Response<Conditions>):
            Resource<Conditions> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG getAllCategories->")
                conditions = resultResponse
                Timber.d("$TAG getAllCategories->response->$conditions")
                Timber.d("$TAG getAllCategories-> Resource.Success->$resultResponse")
                return Resource.Success(conditions ?: resultResponse)
            }
        }
        Timber.e("$TAG getAllCategories->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    private fun getConditions() =
        viewModelScope.launch {
            getConditions(
                share.getString(TOKEN, "")!!
            )
        }

    init {
        getConditions()
    }


}