package com.raiyansoft.eata.network

import com.raiyansoft.eata.model.about.AboutUs
import com.raiyansoft.eata.model.cases.Cases
import com.raiyansoft.eata.model.categories.Categories
import com.raiyansoft.eata.model.conditions.Conditions
import com.raiyansoft.eata.model.contact.Contact
import com.raiyansoft.eata.model.contact.ContactUs
import com.raiyansoft.eata.model.donation.ConfirmDonate
import com.raiyansoft.eata.model.donation.Donation
import com.raiyansoft.eata.model.donation.PostDonate
import com.raiyansoft.eata.model.notification.Notification
import com.raiyansoft.eata.model.privacy.Privacy
import com.raiyansoft.eata.model.profile.Profile
import com.raiyansoft.eata.model.question.QuestionAnswer
import com.raiyansoft.eata.model.save.PostSave
import com.raiyansoft.eata.model.save.SavePost
import com.raiyansoft.eata.model.setting.Setting
import com.raiyansoft.eata.model.status.Status
import com.raiyansoft.eata.model.user.ActivateAccount
import com.raiyansoft.eata.model.user.RegisterUser
import com.raiyansoft.eata.model.user.UserToken
import com.raiyansoft.eata.model.user.resendActivtion.Resend
import com.raiyansoft.eata.model.user.userActivate.DataActivate
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import javax.net.ssl.SSLEngineResult

interface Api {

    @POST("user/register/benefactor")
    suspend fun postBenefactor(
        @Body registerUser: RegisterUser
    ): Response<UserToken>

    @POST("user/register/needy")
    suspend fun postNeedy(
        @Body registerUser: RegisterUser
    ): Response<UserToken>


    @POST("user/activateAccount")
    suspend fun activateAccount(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body activateAccount: ActivateAccount
    ): Response<DataActivate>


    @POST("user/resendActivation")
    suspend fun resendActivation(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<Resend>


    @GET("privacy")
    suspend fun getPrivacy(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<Privacy>

    @GET("conditions")
    suspend fun getConditions(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<Conditions>


    @GET("faq")
    suspend fun getQuestionAnswer(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<QuestionAnswer>


    @GET("user/notification")
    suspend fun getNotification(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("page") page: String
    ): Response<Notification>

    @GET("setting")
    suspend fun getSetting(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<Setting>


    @GET("about")
    suspend fun getAbout(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<AboutUs>

    @POST("cases/addFav")
    suspend fun postSave(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body save: PostSave
    ): Response<SavePost>


    @GET("user/profile")
    suspend fun getProfile(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<Profile>


    @Multipart
    @POST("cases/createCase")
    suspend fun uploadNewCase(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>
    ): Response<Status>

    @Multipart
    @POST("cases/updateCase")
    suspend fun updateCase(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>
    ): Response<Status>


    @GET("cases/myDonation/{id}")
    suspend fun getDonation(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Path("id") id: String,
        @Query("page") page: String
    ): Response<Donation>


    @GET("cases")
    suspend fun getCases(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("page") page: String,
        @Query("cat_id") id: String
    ): Response<Cases>


    @GET("cases")
    suspend fun getCases(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("page") page: String
    ): Response<Cases>

    @GET("cases/myCase")
    suspend fun getMyCase(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("page") page: String
    ): Response<Cases>


    @GET("cases/categories")
    suspend fun getCategories(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("page") page: String
    ): Response<Categories>

    @POST("user/addDonation")
    suspend fun postDonates(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body donate: PostDonate
    ): Response<Donation>

    @POST("user/confirmDonation")
    suspend fun ConfirmDonates(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body donate: ConfirmDonate
    ): Response<Status>


    @GET("cases/getFav")
    suspend fun getSave(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("page") page: String
    ): Response<Cases>

    @POST("cases/deleteFav")
    suspend fun deleteCase(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body donate: PostSave
    ): Response<Status>


    @POST("contactUs")
    suspend fun postContactUs(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body contact: Contact
    ): Response<ContactUs>


    @GET("cases/deleteCase/{id}")
    suspend fun deleteMyCase(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Path("id") id: String
    ): Response<Status>


    @GET("cases/deleteImage/{id}")
    suspend fun deleteImage(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Path("id") id: String
    ): Response<Status>

}