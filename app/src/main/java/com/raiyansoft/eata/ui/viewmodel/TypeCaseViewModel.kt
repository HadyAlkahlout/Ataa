package com.raiyansoft.eata.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.eata.model.cases.Cases
import com.raiyansoft.eata.repository.ApiRepository
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Constants.TYPE
import com.raiyansoft.eata.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class TypeCaseViewModel(application: Application) : AndroidViewModel(application) {
    val TAG = "TypeCaseViewModel"

    private val repository = ApiRepository()
    val dataCaseLiveData = MutableLiveData<Resource<Cases>>()
    var case: Cases? = null
    var page = 1

    private val share = Constants.getSharePref(application.applicationContext)


    private suspend fun getCease(
        Authorization: String, id: String, type: Int
    ) {
        dataCaseLiveData.postValue(Resource.Loading())
        try {

            var response: Response<Cases>
            if (share.getInt(TYPE, 0) == 0)
                if (type == 0) {
                     response = repository
                        .getCases(Authorization, page.toString(), id)
                    Log.e("sadfasdf", "id")

                } else {
                     response = repository
                        .getCases(Authorization, page.toString())
                    Log.e("sadfasdf", "noId")
                }
            else {
                 response = repository
                    .getMyCase(Authorization, page.toString())
                Log.e("sadfasdf", "case")

            }

            dataCaseLiveData.postValue(getCease(response!!))
            Timber.d("$TAG getCease-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataCaseLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG getCease-> Network Failure")
                }
                else -> {
                    dataCaseLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG getCease-> Conversion Error")
                }

            }
        }
    }


    private fun getCease(response: Response<Cases>):
            Resource<Cases> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                page++
                Timber.d("$TAG getCease->page->$case")
                if (case == null) {
                    case = resultResponse
                    Timber.d("$TAG getCease->categories->$case")
                } else {
                    val oldCases = case
                    oldCases!!.data.data.addAll(resultResponse.data.data)
                    Timber.d("$TAG getCease->oldCategories->$oldCases")

                }
                Timber.d("$TAG getCease-> Resource.Success->$resultResponse")
                return Resource.Success(case ?: resultResponse)
            }
        }
        Timber.e("$TAG getCease->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getCease(id: String, type: Int) = viewModelScope.launch {
        getCease(
            share.getString(Constants.TOKEN, "")!!,
            id, type
        )
        Timber.d("$TAG getResponseImages")
    }


}