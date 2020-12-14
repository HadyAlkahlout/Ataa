package com.raiyansoft.eata.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.eata.model.donation.ConfirmDonate
import com.raiyansoft.eata.model.donation.Donation
import com.raiyansoft.eata.model.donation.PostDonate
import com.raiyansoft.eata.model.status.Status
import com.raiyansoft.eata.repository.ApiRepository
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Constants.TOKEN
import com.raiyansoft.eata.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "DetailsViewModel"

    private val repository = ApiRepository()

    val dataPostDonatesLiveData = MutableLiveData<Resource<Donation>>()
    val dataPostConfirmDonatesLiveData = MutableLiveData<Resource<Status>>()
    private var donation: Donation? = null
    private var status_confirm: Status? = null
    private val share = Constants.getSharePref(application.applicationContext)




    private suspend fun PostDonation(
        donate: PostDonate, Authorization: String
    ) {
        dataPostDonatesLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .postDonate(donate, Authorization)

            dataPostDonatesLiveData.postValue(PostDonation(response))
            Timber.d("$TAG getDonation-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataPostDonatesLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG getDonation-> Network Failure")
                }
                else -> {
                    dataPostDonatesLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG getDonation-> Conversion Error")
                }

            }
        }
    }

    private fun PostDonation(response: Response<Donation>):
            Resource<Donation> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG getDonation->")
                donation = resultResponse
                Timber.d("$TAG getDonation->response->$donation")
                Timber.d("$TAG getDonation-> Resource.Success->$resultResponse")
                return Resource.Success(donation ?: resultResponse)
            }
        }
        Timber.e("$TAG getDonation->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun PostDonation(donate: PostDonate) =
        viewModelScope.launch {
            PostDonation(
                donate, share.getString(TOKEN, "")!!
            )
        }


    private suspend fun postConfirmDonate(
        donate: ConfirmDonate, Authorization: String
    ) {
        dataPostConfirmDonatesLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .postConfirmDonate(donate, Authorization)

            dataPostConfirmDonatesLiveData.postValue(postConfirmDonate(response))
            Timber.d("$TAG getDonation-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataPostConfirmDonatesLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getDonation-> Network Failure")
                }
                else -> {
                    dataPostConfirmDonatesLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getDonation-> Conversion Error")
                }

            }
        }
    }

    private fun postConfirmDonate(response: Response<Status>):
            Resource<Status> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG getDonation->")
                status_confirm = resultResponse
                Timber.d("$TAG getDonation->response->$status_confirm")
                Timber.d("$TAG getDonation-> Resource.Success->$resultResponse")
                return Resource.Success(status_confirm ?: resultResponse)
            }
        }
        Timber.e("$TAG getDonation->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun postConfirmDonate(donate: ConfirmDonate) =
        viewModelScope.launch {
            postConfirmDonate(
                donate, share.getString(TOKEN, "")!!
            )
        }


    val dataStatusLiveData = MutableLiveData<Resource<Status>>()
     var status: Status? = null


    private suspend fun deleteCase(
        Authorization: String, id: String, type: Int
    ) {
        dataStatusLiveData.postValue(Resource.Loading())
        try {
            val response =
                if (type == 1)
                    repository
                        .deleteMuyCase(Authorization, id)
                else {
                    repository
                        .deleteImage(Authorization, id)
                }
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


    fun deleteCase(id: String, type: Int) = viewModelScope.launch {
        deleteCase(
            share.getString(TOKEN, "")!!, id, type
        )

    }

}