package com.raiyansoft.eata.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.eata.model.user.ActivateAccount
import com.raiyansoft.eata.model.user.RegisterUser
import com.raiyansoft.eata.model.user.UserToken
import com.raiyansoft.eata.model.user.resendActivtion.Resend
import com.raiyansoft.eata.model.user.userActivate.DataActivate
import com.raiyansoft.eata.repository.ApiRepository
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "AuthViewModel"
    private val repository = ApiRepository()


    val dataUserLiveData = MutableLiveData<Resource<UserToken>>()
    private var userToken: UserToken? = null

    private val share = Constants.getSharePref(application.applicationContext)


    private suspend fun postBenefactor(
        registerUser: RegisterUser
    ) {
        dataUserLiveData.postValue(Resource.Loading())
        try {
            val response =
                if (share.getInt(Constants.TYPE, 0) == 0)
                    repository
                        .postBenefactor(registerUser)
                else
                    repository.postNeedy(registerUser)

            dataUserLiveData.postValue(postBenefactor(response))
            Timber.d("$TAG postBenefactor-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataUserLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG postBenefactor-> Network Failure")
                }
                else -> {
                    dataUserLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG postBenefactor-> Conversion Error")
                }

            }
        }
    }

    private fun postBenefactor(response: Response<UserToken>):
            Resource<UserToken> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG postBenefactor->")
                userToken = resultResponse
                Timber.d("$TAG postBenefactor->response->$userToken")
                Timber.d("$TAG postBenefactor-> Resource.Success->$resultResponse")
                return Resource.Success(userToken ?: resultResponse)
            }
        }
        Timber.e("$TAG postBenefactor->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun postBenefactors(
        registerUser: RegisterUser
    ) =
        viewModelScope.launch {
            postBenefactor(
                registerUser
            )
        }

    val dataStatusLiveData = MutableLiveData<Resource<DataActivate>>()
    private var activate: DataActivate? = null


    private suspend fun activateAccount(
        Authorization: String, activateAccount: ActivateAccount
    ) {
        dataStatusLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .activateAccount(Authorization, activateAccount)

            dataStatusLiveData.postValue(activateAccount(response))
            Timber.d("$TAG postBenefactor-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataStatusLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG postBenefactor-> Network Failure")
                }
                else -> {
                    dataStatusLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG postBenefactor-> Conversion Error")
                }

            }
        }
    }

    private fun activateAccount(response: Response<DataActivate>):
            Resource<DataActivate> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG activateAccount->")
                activate = resultResponse
                Timber.d("$TAG activateAccount->response->$activate")
                Timber.d("$TAG activateAccount-> Resource.Success->$resultResponse")
                return Resource.Success(activate ?: resultResponse)
            }
        }
        Timber.e("$TAG activateAccount->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun activateAccount(
        activateAccount: ActivateAccount
    ) =
        viewModelScope.launch {
            activateAccount(
                share.getString(Constants.TOKEN, "")!!, activateAccount
            )
            Log.e("tttttttttttToken", "${share.getString(Constants.TOKEN, "")}")
        }


    val dataResendLiveData = MutableLiveData<Resource<Resend>>()
    private var resend: Resend? = null


    private suspend fun resendActivation(
        Authorization: String
    ) {
        dataResendLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .resendActivation(Authorization)

            dataResendLiveData.postValue(resendActivation(response))
            Timber.d("$TAG resendActivation-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataResendLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG resendActivation-> Network Failure")
                }
                else -> {
                    dataResendLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG resendActivation-> Conversion Error")
                }

            }
        }
    }

    private fun resendActivation(response: Response<Resend>):
            Resource<Resend> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG resendActivation->")
                resend = resultResponse
                Timber.d("$TAG resendActivation->response->$resend")
                Timber.d("$TAG resendActivation-> Resource.Success->$resultResponse")
                return Resource.Success(resend ?: resultResponse)
            }
        }
        Timber.e("$TAG activateAccount->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun resendActivation(
    ) =
        viewModelScope.launch {
            resendActivation(
                share.getString(Constants.TOKEN, "")!!
            )
        }

}