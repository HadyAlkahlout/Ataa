package com.raiyansoft.eata.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.eata.model.contact.Contact
import com.raiyansoft.eata.model.contact.ContactUs
import com.raiyansoft.eata.repository.ApiRepository
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "ContactUsViewModel"

    private val repository = ApiRepository()

    val contactLiveData = MutableLiveData<Resource<ContactUs>>()
    private var contact: ContactUs? = null
    private val share = Constants.getSharePref(application.applicationContext)


    private suspend fun postContactUs(
        Authorization: String,
        contact: Contact
    ) {
        contactLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .postContactUs(contact, Authorization)
            contactLiveData.postValue(postCon(response))
            Timber.d("$TAG getPrivacy-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    contactLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG getPrivacy-> Network Failure")
                }
                else -> {
                    contactLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG getPrivacy-> Conversion Error")
                }

            }
        }
    }


    private fun postCon(response: Response<ContactUs>):
            Resource<ContactUs> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.e("$TAG getContact->")
                contact = resultResponse
                Timber.e("$TAG getContact->response->$contact")
                Timber.e("$TAG getContact-> Resource.Success->$resultResponse")
                return Resource.Success(contact ?: resultResponse)
            }
        }
        Timber.e("$TAG getContact->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun postContact(contact: Contact) =
        viewModelScope.launch {
            postContactUs(
                share.getString(Constants.TOKEN, "")!!,
                contact
            )
        }


}