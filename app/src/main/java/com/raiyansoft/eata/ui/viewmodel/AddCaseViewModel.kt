package com.raiyansoft.eata.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.eata.model.status.Status
import com.raiyansoft.eata.repository.ApiRepository
import com.raiyansoft.eata.util.Constants.TOKEN
import com.raiyansoft.eata.util.Constants.getSharePref
import com.raiyansoft.eata.util.Resource
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class AddCaseViewModel(application: Application) : AndroidViewModel(application) {


    private val TAG = "AddCaseViewModel"

    private val repository = ApiRepository()

    private val share = getSharePref(application.applicationContext)
    val dataStatusLiveData = MutableLiveData<Resource<Status>>()
    private var status: Status? = null

    private suspend fun uploadNewCase(
        Authorization: String,
        params: Map<String, RequestBody>,
        images: List<MultipartBody.Part>,
        type: Int
    ) {
        dataStatusLiveData.postValue(Resource.Loading())
        try {
            val response =
                if (type == 1)
                    repository
                        .uploadNewCase(Authorization, params, images)
                else
                    repository
                        .updateCase(Authorization, params, images)



            dataStatusLiveData.postValue(uploadNewCase(response))
            Timber.d("$TAG uploadNewCase-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    Log.e("tttttt", "${t.message}")
                    dataStatusLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG uploadNewCase-> Network Failure")
                }
                else -> {
                    dataStatusLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG uploadNewCase-> Conversion Error")
                }

            }
        }
    }

    private fun uploadNewCase(response: Response<Status>):
            Resource<Status> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG uploadNewCase->")
                status = resultResponse
                Timber.d("$TAG uploadNewCase->response->$status")
                Timber.d("$TAG uploadNewCase-> Resource.Success->$resultResponse")
                return Resource.Success(status ?: resultResponse)
            }
        }
        Timber.e("$TAG postBenefactor->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun uploadNewCase(
        params: Map<String, RequestBody>,
        images: List<MultipartBody.Part>,
        i: Int
    ) =
        viewModelScope.launch {
            uploadNewCase(
                share.getString(TOKEN, "")!!,
                params, images, i
            )
        }


}