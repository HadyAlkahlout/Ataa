package com.raiyansoft.eata.ui.fragment.account

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.eata.databinding.FragmentCallUsBinding
import com.raiyansoft.eata.model.contact.Contact
import com.raiyansoft.eata.ui.viewmodel.ContactViewModel
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.fragment_call_us.*
import kotlinx.android.synthetic.main.fragment_notification.toolbar
import timber.log.Timber

class CallUsFragment : Fragment() {


    private lateinit var mBinding: FragmentCallUsBinding
    private val viewModel by lazy {
        ViewModelProvider(this)[ContactViewModel::class.java]
    }
    private val share by lazy {
        Constants.getSharePref(requireContext())

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCallUsBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.contactLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            if (data.status) {
                                etxt_call_reason.text.clear()
                                etxt_your_message.text.clear()
                                Snackbar.make(
                                    requireView(),
                                    "طلبك قيد المراجعة",
                                    Snackbar.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                    is Resource.Error -> {
                        Log.e("ttttttTow", "${response.message}")

                    }
                    is Resource.Loading -> {
                        Timber.d("onViewCreated-> Resource.Loading")
                    }
                }
            })

        btn_send_message.setOnClickListener {
            val reason = etxt_call_reason.text.toString()
            val message = etxt_your_message.text.toString()

            if (reason.isEmpty()) {
                etxt_call_reason.error = "هذا الحقل مطلوب"
                etxt_call_reason.requestFocus()
                return@setOnClickListener
            }
            if (message.isEmpty()) {
                etxt_your_message.error = "هذا الحقل مطلوب"
                etxt_your_message.requestFocus()
                return@setOnClickListener
            }

            viewModel.postContact(
                Contact(
                    share.getString(Constants.NAME, "").toString(),
                    share.getString(Constants.PHONE_NUMBER, "").toString(),
                    " ",
                    "reason: $reason \n message: $message"
                )
            )
        }


    }


}

