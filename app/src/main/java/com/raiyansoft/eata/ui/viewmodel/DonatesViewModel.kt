package com.raiyansoft.eata.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.eata.model.donation.Donation
import com.raiyansoft.eata.repository.ApiRepository
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Constants.TOKEN
import com.raiyansoft.eata.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class DonatesViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "DonatesViewModel"

    private val repository = ApiRepository()

    val dataDonatesLiveData = MutableLiveData<Resource<Donation>>()
    private var donation: Donation? = null
    private val share = Constants.getSharePref(application.applicationContext)
    private var page = 1

    private suspend fun getDonation(
        Authorization: String
    ) {
        dataDonatesLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getDonation(
                    Authorization,
                    if (share.getInt(Constants.TYPE, 0) == 0) "1" else "2", page.toString()
                )

            dataDonatesLiveData.postValue(getDonation(response))
            Timber.d("$TAG getDonation-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataDonatesLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG getDonation-> Network Failure")
                }
                else -> {
                    dataDonatesLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG getDonation-> Conversion Error")
                }

            }
        }
    }

    private fun getDonation(response: Response<Donation>):
            Resource<Donation> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                page++
                Timber.d("$TAG getDonation->")
                if (donation == null) {
                    donation = resultResponse
                    Timber.d("$TAG getDonation->response->$donation")
                } else {
                    val old = donation
                    old!!.data.data.addAll(resultResponse.data.data)
                    Timber.d("$TAG getCategories->oldCategories->$old")
                }
                Timber.d("$TAG getDonation-> Resource.Success->$resultResponse")
                return Resource.Success(donation ?: resultResponse)
            }
        }
        Timber.e("$TAG getDonation->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getDonation() =
        viewModelScope.launch {
            getDonation(
                share.getString(TOKEN, "")!!
            )
        }

    init {
        getDonation()
    }


}