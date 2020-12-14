package com.raiyansoft.eata.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.raiyansoft.eata.R

object Constants {

    const val BaseUrl = "https://fcm.googleapis.com/"
    const val ServerKey =
        "AAAAlUED4Vw:APA91bEt0qi9kTj4P08eYsdhmdfY_eYrZz9xQgQ_rywLSnviEMee0VFXHba0ZnFsqPclTME-1HQq1YViTe_f3Q5LjBz6T1zyCK8sfx1Sh5NFqPE6eXjv1ZBQZrrQByLPZOx8DY2wbMfd"
    const val Content_type = "application/json"

    var nav = 0
    const val LOGIN = "login"
    const val TOKEN = "token"
    const val UserID = "userId"
    const val TYPE = "TYPE"
    const val TOKEN_MESSAGE = "tokenMessage"
    const val TYPE_CASES = "typeCase"
    const val DETALIS = "detalis"
    const val DONATES = "donates"
    const val QUERY_PAGE_SIZE = 10
    const val NAME = "name"
    const val PHONE_NUMBER = "phoneNumber"

    fun getSharePref(context: Context) =
        context.getSharedPreferences("Share", Activity.MODE_PRIVATE)

    fun editor(context: Context) = getSharePref(context).edit()

    lateinit var dialog: Dialog
    fun showDialog(activity: Activity) {
        dialog = Dialog(activity).apply {
            setContentView(R.layout.dialog_loading)
            setCancelable(false)
        }
        dialog.show()
    }

}