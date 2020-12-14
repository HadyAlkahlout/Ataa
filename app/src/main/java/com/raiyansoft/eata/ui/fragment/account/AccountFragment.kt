package com.raiyansoft.eata.ui.fragment.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.raiyansoft.eata.R
import com.raiyansoft.eata.databinding.FragmentAccountBinding
import com.raiyansoft.eata.ui.activity.AuthActivity
import com.raiyansoft.eata.ui.viewmodel.ProfileViewModel
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Constants.editor
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_donates.*
import timber.log.Timber

class AccountFragment : Fragment() {

    private lateinit var mBinding: FragmentAccountBinding
    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
    }

    lateinit var name: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentAccountBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    private val share by lazy {
        Constants.getSharePref(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel.dataProfileLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            if (data.status) {
                                name = data.data.name
                                txtNameAccount.text = "معلوماتي: ${data.data.name}"
                                txtNumberPhone.text = "رقم الهاتف : ${data.data.mobile}"
                                txtDate.text = "تاريخ التسجيل : ${data.data.date}"
                            }
                        }
                    }
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {
                        Timber.d("onViewCreated-> Resource.Loading")
                    }
                }
            })

        txtQuestionRe.setOnClickListener {
            navController(R.id.action_mainFragment_to_questionFragment)
        }

        txtTermsUse.setOnClickListener {
            navController(R.id.action_mainFragment_to_conditionsFragment)

        }

        txtPrivacyPolicy.setOnClickListener {
            navController(R.id.action_mainFragment_to_privacyFragment)

        }
        txtNotification.setOnClickListener {
            navController(R.id.action_mainFragment_to_notificationFragment)
        }

        txtHistory.setOnClickListener {
            parentFragment?.parentFragment?.findNavController()
                ?.navigate(R.id.action_mainFragment_to_donatesFragment, Bundle()
                    .apply {
                        putString("yes", "yes")
                    })
        }
        txtContactUs.setOnClickListener {
            navController(R.id.action_mainFragment_to_callUsFragment)
        }

        txtAboutUs.setOnClickListener {
            navController(R.id.action_mainFragment_to_aboutFragment)

        }

        if ((share.getInt(Constants.TYPE, 0) == 0)) {
            txtHistory.visibility = View.VISIBLE
        } else {
            txtHistory.visibility = View.GONE
        }
        txtMessage.visibility = View.GONE
        txtMessage.setOnClickListener {
            navController(R.id.action_mainFragment_to_myMessageFragment)
        }

        txtLogOut.setOnClickListener {
            editor(requireContext()).clear().apply()
            requireActivity().apply {
                startActivity(Intent(this, AuthActivity::class.java)).also {
                    finish()
                }
            }
        }

    }


    private fun navController(id: Int) {
        parentFragment?.parentFragment?.findNavController()
            ?.navigate(id)
    }
}