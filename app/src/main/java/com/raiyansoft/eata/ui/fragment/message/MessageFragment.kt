package com.raiyansoft.eata.ui.fragment.message

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.raiyansoft.eata.R
import com.raiyansoft.eata.adapter.MessageAdapter
import com.raiyansoft.eata.databinding.FragmentMessageBinding
import com.raiyansoft.eata.message.NotificationData
import com.raiyansoft.eata.message.PushNotification
import com.raiyansoft.eata.model.cases.DataX
import com.raiyansoft.eata.model.chat.TextMessage
import com.raiyansoft.eata.model.donation.ConfirmDonate
import com.raiyansoft.eata.model.donation.Content
import com.raiyansoft.eata.model.donation.PostDonate
import com.raiyansoft.eata.ui.fragment.dialog.DonatesDialogFragment
import com.raiyansoft.eata.ui.fragment.dialog.DoneDialogFragment
import com.raiyansoft.eata.ui.fragment.dialog.PaidDialogFragment
import com.raiyansoft.eata.ui.viewmodel.ChatViewModel
import com.raiyansoft.eata.ui.viewmodel.DetailsViewModel
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Constants.NAME
import com.raiyansoft.eata.util.Constants.TOKEN_MESSAGE
import com.raiyansoft.eata.util.Constants.TYPE
import com.raiyansoft.eata.util.Constants.UserID
import com.raiyansoft.eata.util.Constants.dialog
import com.raiyansoft.eata.util.Constants.getSharePref
import com.raiyansoft.eata.util.Constants.showDialog
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.fragment_message.*
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MessageFragment : Fragment(), DonatesDialogFragment.GoFragmentMessage,
    PaidDialogFragment.GoFragmentMessage, DoneDialogFragment.GoFragmentMessage {

    private lateinit var mBinding: FragmentMessageBinding


    private val bundle by lazy {
        requireArguments()
    }

    private val arg by lazy {
        bundle.getParcelable<DataX>(Constants.DETALIS)
    }

    private val arg_donate by lazy {
        bundle.getParcelable<Content>(Constants.DONATES)
    }


    private val viewModel by lazy {
        ViewModelProvider(this)[ChatViewModel::class.java]
    }

    private val adapter by lazy {
        MessageAdapter(requireContext(), ArrayList())
    }

    private val viewModelCon by lazy {
        ViewModelProvider(this)[DetailsViewModel::class.java]
    }

    private val share by lazy {
        getSharePref(requireActivity())
    }

    override fun onResume() {
        super.onResume()

        viewModel.getChat.observe(requireActivity(), androidx.lifecycle.Observer {
            adapter.data.clear()
            adapter.data.addAll(it)
            adapter.notifyDataSetChanged()
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentMessageBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }


        if (share.getInt(TYPE, 0) == 0) {
            txtFinishCase.visibility = View.VISIBLE
        } else {
            txtFinishCase.visibility = View.GONE
        }

        rcDataMessage.adapter = adapter

        if (bundle.getBoolean("donate_status") == false) {
            editTextTextPersonName.isEnabled = false
            fabSend.isEnabled = false
            txtFinishCase.visibility=View.GONE
        }

        if (bundle.getString("type") == "details") {
            viewModel.getChat(
                getSharePref(requireContext()).getString(UserID, "")!!,
                arg!!.user_id!!,
                arg!!.id.toString()
            )
            DoMessage(
                getSharePref(requireContext()).getString(UserID, "")!!,
                arg!!.user_id!!,
                arg!!.id.toString(),
                arg!!.user_fcm_token!!, share.getString(NAME, "").toString()
            )
            titleName.text = arg!!.user_name
            setHasOptionsMenu(false)
        } else {
            viewModel.getChat(arg_donate!!.user_id, arg_donate!!.need_id, arg_donate!!.case_id)
            DoMessage(
                arg_donate!!.user_id,
                arg_donate!!.need_id,
                arg_donate!!.case_id,
                if (getSharePref(requireContext()).getInt(Constants.TYPE,0) == 0){
                    arg_donate!!.need_fcm
                }else{
                    arg_donate!!.user_fcm
                },
                share.getString(NAME, "").toString()
            )
            titleName.text = "صفحة الشات"
            setHasOptionsMenu(true)
        }

        viewModelCon.dataPostDonatesLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            dialog.hide()
                            if (data.status) {
                                Snackbar.make(
                                    mBinding.root,
                                    "تم تأكيد التبرع",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    is Resource.Error -> {
                        showDialog(requireActivity())
                        dialog.hide()
                    }
                    is Resource.Loading -> {
                        Timber.d("onViewCreated-> Resource.Loading")
                        showDialog(requireActivity())
                    }
                }
            })


        txtFinishCase.setOnClickListener {

            if (bundle.getString("type") == "details") {
                DonatesDialogFragment(
                    this,
                    arg!!.id.toString(),
                    arg!!.total!!.substring(0, arg!!.total!!.indexOf("."))
                ).show(requireActivity().supportFragmentManager, "")
                Log.e("eee total price",arg!!.total!!.substring(0, arg!!.total!!.indexOf(".")))

            } else {
                DonatesDialogFragment(
                    this,
                    arg_donate!!.id.toString(),
                    arg_donate!!.total.toString().substring(0, arg!!.total!!.indexOf("."))
                ).show(requireActivity().supportFragmentManager, "")

            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun DoMessage(
        userID: String,
        CaseUserID: String,
        CaseID: String, token: String, name: String
    ) {

        fabSend.setOnClickListener {
            if (editTextTextPersonName.text.toString().isEmpty()) {
                return@setOnClickListener
            }

            viewModel.CreateChatChannel(
                userID,
                CaseUserID,
                CaseID,
                editTextTextPersonName.text.toString()
                , name
            ) {

                PushNotification(
                    NotificationData(
                        "New message from ${name}",
                        editTextTextPersonName.text.toString()
                    ),
                    token
                ).also {
                    PushNotification().Notification().sendNotification(it)
                }.also {
                    editTextTextPersonName.text.clear()
                }

            }

        }

    }

    override fun onClick(code: String, total: String) {
        PaidDialogFragment(
            this,
            arg!!.id.toString()
        ,total).show(requireActivity().supportFragmentManager, "")
        Log.e("eee price",total)
    }

    override fun onClickGo(code: String, total: String) {
        Log.e("eee price done",total)
        DoneDialogFragment(
            this
        ).show(requireActivity().supportFragmentManager, "")
    }

    override fun onClickDo(code: String, total: String) {
        findNavController().navigateUp()
        findNavController().navigateUp()
    }


}