package com.raiyansoft.eata.ui.fragment.auth

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.iid.FirebaseInstanceId
import com.raiyansoft.eata.R
import com.raiyansoft.eata.databinding.FragmentLoginBinding
import com.raiyansoft.eata.model.user.RegisterUser
import com.raiyansoft.eata.ui.viewmodel.AuthViewModel
import com.raiyansoft.eata.util.Constants.NAME
import com.raiyansoft.eata.util.Constants.PHONE_NUMBER
import com.raiyansoft.eata.util.Constants.TOKEN
import com.raiyansoft.eata.util.Constants.TOKEN_MESSAGE
import com.raiyansoft.eata.util.Constants.UserID
import com.raiyansoft.eata.util.Constants.dialog
import com.raiyansoft.eata.util.Constants.editor
import com.raiyansoft.eata.util.Constants.showDialog
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.fragment_login.*
import timber.log.Timber


class LoginFragment : Fragment(), OnBackClickListener {


    private lateinit var mBinding: FragmentLoginBinding
    private val viewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    private val edit
            by lazy { editor(requireActivity()) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentLoginBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView2.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.slide_up
            )
        )

        viewModel.dataUserLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            if (data.status) {
                                dialog.hide()


                                Log.e("ttttttttTokendata", "${data.data.token}")
                                val bundel = Bundle()
                                bundel.putBoolean("Start", true)

                                edit.putString(TOKEN, "Bearer " + data.data.token)
                                edit.putString(UserID, data.data.userId.toString())
                                edit.apply()

                                Log.e("eee userID", data.data.token.toString())

                                findNavController().navigate(
                                    R.id.action_loginFragment_to_verificationFragment, bundel
                                )

                            }
                        }
                    }
                    is Resource.Error -> {
                        dialog.hide()
                    }
                    is Resource.Loading -> {
                        Timber.d("onViewCreated-> Resource.Loading")
                        showDialog(requireActivity())
                    }
                }
            })

        btnLogin.setOnClickListener {
            val name = txtNameAccount.text.toString()
            val number = txtNumberPhone.text.toString()
            if (name.isEmpty()) {
                txtNameAccount.error = "هذا الحقل مطلوب"
                txtNameAccount.requestFocus()
                return@setOnClickListener
            }
            if (number.isEmpty()) {
                txtNumberPhone.error = "هذا الحقل مطلوب"
                txtNumberPhone.requestFocus()
                return@setOnClickListener
            }
            if (number.length != 8) {
                txtNumberPhone.error = "يجب ان يحتوي الرقم على 8 رقم"
                txtNumberPhone.requestFocus()
                return@setOnClickListener
            }



            edit.putString(NAME, name)
            edit.putString(PHONE_NUMBER, number)
            edit.apply()

            val bundel = Bundle()
            bundel.putBoolean("Start", true)

            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { token ->
                if (token.isSuccessful) {
                    val tokenMessage = token.result!!.token
                    edit.putString(TOKEN_MESSAGE, tokenMessage)
                    viewModel.postBenefactors(
                        RegisterUser(
                            name,
                            "00965$number", "android", tokenMessage, "00965"
                        )
                    ).also {
                        Timber.e("eee token $tokenMessage")
                    }
                }
            }

        }
    }
    private var backButtonHandler: BackButtonHandlerInterface? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        backButtonHandler = activity as BackButtonHandlerInterface?
        backButtonHandler!!.addBackClickListener(this)
    }

    override fun onDetach() {
        super.onDetach()
        backButtonHandler!!.removeBackClickListener(this)
        backButtonHandler = null
    }

    override fun onBackClick(): Boolean {
        findNavController().navigateUp()
        findNavController().navigateUp()
        return false
    }

}

interface OnBackClickListener {
    fun onBackClick(): Boolean
}


interface BackButtonHandlerInterface {
    fun addBackClickListener(onBackClickListener: OnBackClickListener)
    fun removeBackClickListener(onBackClickListener: OnBackClickListener)
}