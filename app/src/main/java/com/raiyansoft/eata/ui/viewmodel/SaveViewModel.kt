package com.raiyansoft.eata.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.eata.model.cases.Cases
import com.raiyansoft.eata.model.save.PostSave
import com.raiyansoft.eata.model.save.SavePost
import com.raiyansoft.eata.model.status.Status
import com.raiyansoft.eata.repository.ApiRepository
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class SaveViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "SaveViewModel"

    private val repository = ApiRepository()

    val SaveLiveData = MutableLiveData<Resource<SavePost>>()
    private var post: SavePost? = null
    private val share = Constants.getSharePref(application.applicationContext)

    private suspend fun postSave(
        Authorization: String,
        case_id: PostSave
    ) {
        SaveLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .postSave(case_id, Authorization)
            SaveLiveData.postValue(postSave(response))
            Timber.d("$TAG postSave-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    SaveLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG postSave-> Network Failure")
                }
                else -> {
                    SaveLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG postSave-> Conversion Error")
                }

            }
        }
    }


    private fun postSave(response: Response<SavePost>):
            Resource<SavePost> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.e("$TAG postSave->")
                post = resultResponse
                Timber.e("$TAG postSave->response->$post")
                Timber.e("$TAG postSave-> Resource.Success->$resultResponse")
                return Resource.Success(post ?: resultResponse)
            }
        }
        Timber.e("$TAG postSave->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun postSave(case_id: PostSave) =
        viewModelScope.launch {
            postSave(share.getString(Constants.TOKEN, "")!!, case_id)
        }


    val dataGeSaveLiveData = MutableLiveData<Resource<Cases>>()
    var getSave: Cases? = null
    var page = 1


    private suspend fun getSave(
        Authorization: String
    ) {
        dataGeSaveLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getSave(Authorization, page.toString())

            dataGeSaveLiveData.postValue(getSave(response))
            Timber.d("$TAG getSave-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataGeSaveLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getSave-> Network Failure")
                }
                else -> {
                    dataGeSaveLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getSave-> Conversion Error")
                }

            }
        }
    }

    private fun getSave(response: Response<Cases>):
            Resource<Cases> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                page++
                Timber.d("$TAG getSave->page->$getSave")
                if (getSave == null) {
                    getSave = resultResponse
                    Timber.d("$TAG getSave->categories->$getSave")
                } else {
                    val oldImage = getSave
                    oldImage!!.data.data.addAll(resultResponse.data.data)
                    Timber.d("$TAG getSave->oldCategories->$oldImage")

                }
                Timber.d("$TAG getSave-> Resource.Success->$resultResponse")
                return Resource.Success(getSave ?: resultResponse)
            }
        }
        Timber.e("$TAG getCategories->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getSave() = viewModelScope.launch {
        getSave(
            share.getString(Constants.TOKEN, "").toString().also {
                Log.e("tttttToken", it)
            }
        )
        Timber.d("$TAG getResponseImages")
    }


    val dataStatusLiveData = MutableLiveData<Resource<Status>>()
    var status: Status? = null


    private suspend fun deleteCase(
        Authorization: String, case_id: PostSave

    ) {
        dataStatusLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .deleteCase(case_id, Authorization)

            dataStatusLiveData.postValue(deleteCase(response))
            Timber.d("$TAG deleteCase-> OK")

        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataStatusLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG deleteCase-> Network Failure")
                }
                else -> {
                    dataStatusLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG deleteCase-> Conversion Error")
                }
            }
        }
    }

    private fun deleteCase(response: Response<Status>):
            Resource<Status> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->

                Timber.d("$TAG deleteCase->page->$status")
                status = resultResponse
                Timber.d("$TAG deleteCase->categories->$status")
                return Resource.Success(status ?: resultResponse)
            }
        }
        Timber.e("$TAG getCategories->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun deleteCase(case_id: PostSave) = viewModelScope.launch {
        deleteCase(
            share.getString(Constants.TOKEN, "").toString(), case_id
        )
    }


}