package com.raiyansoft.eata.ui.fragment.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.raiyansoft.eata.R
import com.raiyansoft.eata.databinding.FragmentLoginAsBinding
import com.raiyansoft.eata.ui.activity.MainActivity
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Constants.TYPE
import com.raiyansoft.eata.util.Constants.editor
import kotlinx.android.synthetic.main.fragment_login_as.*


class LoginAsFragment : Fragment() {


    private lateinit var mBinding: FragmentLoginAsBinding
    private val share by lazy {
        Constants.getSharePref(requireContext())
    }

    private val edit by lazy {
        editor(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentLoginAsBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        if (share.getBoolean(Constants.LOGIN, false)) {
            startActivity(Intent(requireContext(), MainActivity::class.java)).also {
                requireActivity().finish()
            }
        }

        tv_logo.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.slide_up
            )
        )

        btn_volunteer.setOnClickListener {
            edit.putInt(TYPE, 0).apply()
            findNavController().navigate(R.id.action_loginAsFragment_to_welcomeFragment)
        }

        btn_pauper.setOnClickListener {
            edit.putInt(TYPE, 1).apply()
            findNavController().navigate(R.id.action_loginAsFragment_to_loginFragment)
        }

    }



}