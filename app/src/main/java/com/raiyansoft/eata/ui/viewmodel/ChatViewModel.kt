package com.raiyansoft.eata.ui.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.raiyansoft.eata.model.chat.TextMessage
import com.raiyansoft.eata.model.chat.User_Chat
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Constants.TYPE
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val _getChat = MutableLiveData<List<TextMessage>>()

    val getChat: LiveData<List<TextMessage>>
        get() = _getChat


    private val _getMyChat = MutableLiveData<List<User_Chat>>()

    val getMyChat: LiveData<List<User_Chat>>
        get() = _getMyChat


    private val share = Constants.getSharePref(application)

    fun getChat(
        userID: String,
        CaseUserID: String,
        CaseID: String
    ) {
        val array = ArrayList<TextMessage>()
        FirebaseFirestore.getInstance().collection("chat")
            .document(
                CaseUserID + "_" + userID
            )
            .collection(CaseID)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                array.clear()
                querySnapshot?.documents!!.forEach {
                    val item = it.toObject(TextMessage::class.java)
                    array.add(item!!)
                }
                _getChat.postValue(array)
            }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun CreateChatChannel(
        userID: String,
        CaseUserID: String,
        CaseID: String,
        message: String,
        toName: String,
        onComplete: (channelId: String) -> Unit
    ) {
        FirebaseFirestore.getInstance().collection("chat")
            .document(
                CaseUserID + "_" + userID
            ).collection(CaseID).document(CaseID).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    onComplete(it[CaseID] as String)
                    return@addOnSuccessListener
                }

            }


      /*  val date = Date().toString()
        val formatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH)
        val localDate: LocalDateTime = LocalDateTime.parse(date, formatter)
        val timeInMilliseconds: Long =
            localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()
        Log.e("eee time ", "Date in milli :: FOR API >= 26 >>> $timeInMilliseconds")*/

        Sendmessage(
            userID,
            CaseUserID,
            CaseID,
            TextMessage(
                CaseID,
                Calendar.getInstance().timeInMillis.toString(),
                if (share.getInt(TYPE, 0) == 0) {
                    userID
                } else {
                    CaseUserID
                },
                message,
                if (share.getInt(TYPE, 0) == 0) {
                    CaseUserID
                } else {
                    userID
                },
                toName
            )
        )

        onComplete("")

    }


    fun Sendmessage(
        userID: String,
        CaseUserID: String,
        CaseID: String,
        message: TextMessage?
    ){
        FirebaseFirestore.getInstance().collection("chat")
            .document(
                CaseUserID + "_" + userID

            )
            .collection(CaseID)
            .add(message!!).addOnSuccessListener {

            }.addOnFailureListener {

            }
    }


}