package com.raiyansoft.eata.repository

import com.raiyansoft.eata.model.contact.Contact
import com.raiyansoft.eata.model.donation.ConfirmDonate
import com.raiyansoft.eata.model.donation.PostDonate
import com.raiyansoft.eata.model.save.PostSave
import com.raiyansoft.eata.model.user.ActivateAccount
import com.raiyansoft.eata.model.user.RegisterUser
import com.raiyansoft.eata.network.RetrofitInstance
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.PartMap

class ApiRepository {

    suspend fun postBenefactor(registerUser: RegisterUser) =
        RetrofitInstance.api!!.postBenefactor(registerUser)

    suspend fun postNeedy(registerUser: RegisterUser) =
        RetrofitInstance.api!!.postNeedy(registerUser)

    suspend fun activateAccount(Authorization: String, activateAccount: ActivateAccount) =
        RetrofitInstance.api!!.activateAccount("ar", Authorization, activateAccount)

    suspend fun resendActivation(Authorization: String) =
        RetrofitInstance.api!!.resendActivation("ar", Authorization)


    suspend fun getPrivacy(
        Authorization: String
    ) = RetrofitInstance.api!!
        .getPrivacy("ar", Authorization)

    suspend fun getConditions(
        Authorization: String
    ) = RetrofitInstance.api!!.getConditions("ar", Authorization)

    suspend fun getQuestionAnswer(
        Authorization: String
    ) = RetrofitInstance.api!!.getQuestionAnswer("ar", Authorization)


    suspend fun getNotification(
        Authorization: String, page: String
    ) = RetrofitInstance.api!!
        .getNotification("ar", Authorization, page)

    suspend fun getSetting(Authorization: String) =
        RetrofitInstance.api!!.getSetting("ar", Authorization)

    suspend fun getProfile(Authorization: String) =
        RetrofitInstance.api!!.getProfile("ar", Authorization)

    suspend fun uploadNewCase(
        Authorization: String,
        params: Map<String, RequestBody>,
        images: List<MultipartBody.Part>
    ) =
        RetrofitInstance.api!!.uploadNewCase(
            "ar", Authorization, params, images
        )


    suspend fun updateCase(
        Authorization: String,
        params: Map<String, RequestBody>,
        images: List<MultipartBody.Part>
    ) =
        RetrofitInstance.api!!.updateCase(
            "ar", Authorization, params, images
        )

    suspend fun getDonation(Authorization: String, id: String, page: String) =
        RetrofitInstance.api!!.getDonation("ar", Authorization, id, page)


    suspend fun getCategories(Authorization: String, page: String) =
        RetrofitInstance.api!!.getCategories("ar", Authorization,page)

    suspend fun getCases(Authorization: String, page: String, id: String) =
        RetrofitInstance.api!!.getCases("ar", Authorization, page, id)

    suspend fun getCases(Authorization: String, page: String) =
        RetrofitInstance.api!!.getCases("ar", Authorization, page)

    suspend fun getMyCase(Authorization: String, page: String) =
        RetrofitInstance.api!!.getMyCase("ar", Authorization, page)


    suspend fun getAbout(
        Authorization: String
    ) = RetrofitInstance.api!!.getAbout("ar", Authorization)


    suspend fun postSave(post: PostSave, authorization: String) =
        RetrofitInstance.api!!.postSave("ar", authorization, post)


    suspend fun getSave(Authorization: String, page: String) =
        RetrofitInstance.api!!.getSave("ar", Authorization, page)


    suspend fun postDonate(donate: PostDonate, authorization: String) =
        RetrofitInstance.api!!.postDonates("ar", authorization, donate)

    suspend fun postConfirmDonate(donate: ConfirmDonate, authorization: String) =
        RetrofitInstance.api!!.ConfirmDonates("ar", authorization, donate)

    suspend fun deleteCase(donate: PostSave, authorization: String) =
        RetrofitInstance.api!!.deleteCase("ar", authorization, donate)

    suspend fun postContactUs(contact: Contact, authorization: String) =
        RetrofitInstance.api!!.postContactUs("ar", authorization, contact)


    suspend fun deleteMuyCase(authorization: String, id: String) =
        RetrofitInstance.api!!.deleteMyCase("ar", authorization, id)

    suspend fun deleteImage(authorization: String, id: String) =
        RetrofitInstance.api!!.deleteImage("ar", authorization, id)

}