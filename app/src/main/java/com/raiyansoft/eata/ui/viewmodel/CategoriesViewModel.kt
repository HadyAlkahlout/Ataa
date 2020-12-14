package com.raiyansoft.eata.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.eata.model.categories.Categories
import com.raiyansoft.eata.repository.ApiRepository
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {

    val TAG = "CategoriesViewModel"

    private val repository = ApiRepository()

    val dataCategoriesLiveData = MutableLiveData<Resource<Categories>>()
    private var categories: Categories? = null

    private val share = Constants.getSharePref(application.applicationContext)


    private var page = 1


    private suspend fun getCategories(
        Authorization: String
    ) {
        dataCategoriesLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getCategories(Authorization, page.toString())

            dataCategoriesLiveData.postValue(getCategories(response))
            Timber.d("$TAG getCategories-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataCategoriesLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG getCategories-> Network Failure")
                }
                else -> {
                    dataCategoriesLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getCategories-> Conversion Error")
                }

            }
        }
    }

    private fun getCategories(response: Response<Categories>):
            Resource<Categories> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                page++
                Timber.d("$TAG getCategories->page->$categories")
                if (categories == null) {
                    categories = resultResponse
                    Timber.d("$TAG getCategories->categories->$categories")
                } else {
                    val oldImage = categories
                    oldImage!!.data.data.addAll(resultResponse.data.data)
                    Timber.d("$TAG getCategories->oldCategories->$oldImage")

                }
                Timber.d("$TAG getCategories-> Resource.Success->$resultResponse")
                return Resource.Success(categories ?: resultResponse)
            }
        }
        Timber.e("$TAG getCategories->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getCategories() = viewModelScope.launch {
        getCategories(
            share.getString(Constants.TOKEN, "")!!        )
        Timber.d("$TAG getResponseImages")
    }

    init {

    }
}