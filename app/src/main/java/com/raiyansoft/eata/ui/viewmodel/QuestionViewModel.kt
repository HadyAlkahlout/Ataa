package com.raiyansoft.eata.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.eata.model.question.QuestionAnswer
import com.raiyansoft.eata.repository.ApiRepository
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Constants.TOKEN
import com.raiyansoft.eata.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class QuestionViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "QuestionViewModel"

    private val repository = ApiRepository()

    val dataQuestionLiveData = MutableLiveData<Resource<QuestionAnswer>>()
    private var question: QuestionAnswer? = null
    private val share = Constants.getSharePref(application.applicationContext)


    private suspend fun getQuestionAnswer(
        Authorization: String
    ) {
        dataQuestionLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getQuestionAnswer(Authorization)
            dataQuestionLiveData.postValue(getQuestionAnswer(response))
            Timber.d("$TAG getAllCategories-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataQuestionLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG getAllCategories-> Network Failure")
                }
                else -> {
                    dataQuestionLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG getAllCategories-> Conversion Error")
                }

            }
        }
    }

    private fun getQuestionAnswer(response: Response<QuestionAnswer>):
            Resource<QuestionAnswer> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG getAllCategories->")
                question = resultResponse
                Timber.d("$TAG getAllCategories->response->$question")
                Timber.d("$TAG getAllCategories-> Resource.Success->$resultResponse")
                return Resource.Success(question ?: resultResponse)
            }
        }
        Timber.e("$TAG getAllCategories->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    private fun getQuestionAnswer() =
        viewModelScope.launch {
            getQuestionAnswer(
                share.getString(TOKEN, "")!!
            )
        }

    init {
        getQuestionAnswer()
    }

}