package com.raiyansoft.eata.network


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {


    companion object {
        private const val BASE_URL = "https://ataa-app.com/api/"

        var api: Api? = null
//        var notificationApi: Api? = null

        init {

            val client = OkHttpClient.Builder()
                .build()

            api = getInstantRetrofit(BASE_URL, client).create(Api::class.java)
//            notificationApi = getInstantRetrofit(BASE_URL_MESSAGE, client).create(Api::class.java)


        }

        private fun getInstantRetrofit(url: String, client: OkHttpClient) =
            Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()


    }

}