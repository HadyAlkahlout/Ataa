package com.raiyansoft.eata.ui.fragment.auth

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.raiyansoft.eata.R
import com.raiyansoft.eata.databinding.FragmentVerificationBinding
import com.raiyansoft.eata.model.user.ActivateAccount
import com.raiyansoft.eata.ui.activity.MainActivity
import com.raiyansoft.eata.ui.viewmodel.AuthViewModel
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.fragment_verification.*
import timber.log.Timber
import java.util.*


class VerificationFragment : Fragment() {


    private lateinit var mBinding: FragmentVerificationBinding
    private var mCountDownTimer: CountDownTimer? = null

    private var mStartTimeInMillis: Long = 0
    private var mTimeLeftInMillis: Long = 0
    private var mEndTime: Long = 0
    private var mTimerRunning = true

    private val viewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private val edit
            by lazy { Constants.editor(requireActivity()) }


    private val prefs: SharedPreferences by lazy {
        requireActivity().getSharedPreferences(
            "prefs",
            MODE_PRIVATE
        )
    }
    private val editor
        get() = prefs.edit()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentVerificationBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }


        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_image_verify.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.fragment_open_enter
            )
        )

        viewModel.dataStatusLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.e(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.e(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            Log.e("tttttttt", "${data.code}")
                            Constants.dialog.hide()
                            if (data.status) {
                                edit.putBoolean(Constants.LOGIN, true)
                                edit.apply()
                                startActivity(
                                    Intent(
                                        requireActivity(),
                                        MainActivity::class.java
                                    )
                                ).also {
                                    requireActivity().finish()
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        Timber.e("onViewCreated-> Resource.Error ${response.message!!}")
                        Constants.dialog.hide()

                    }
                    is Resource.Loading -> {
                        Timber.e("onViewCreated-> Resource.Loading")
                        Constants.showDialog(requireActivity())

                    }
                }
            })

        btn_verify_now.setOnClickListener {
            val activation_code = PinView.text.toString()
            if (activation_code.isEmpty()) {
                Toast.makeText(requireContext(), "يرجى إدخال رمز التأكيد", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            viewModel.activateAccount(ActivateAccount(activation_code))
        }


        viewModel.dataResendLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            Timber.e("onViewCreated-> Resource.Error ${data.code}")
                            if (data.status) {
                                Log.e("eee code", data.status.toString())
                            }
                        }
                    }
                    is Resource.Error -> {
                        Log.e("eee code", response.data!!.status.toString())
                        Timber.e("onViewCreated-> Resource.Error ${response.message!!}")

                    }
                    is Resource.Loading -> {
                        Timber.e("onViewCreated-> Resource.Loading")
                    }
                }
            })




        txtResendCode.setOnClickListener {
            setTime(300000)
            startTimer()
            txtResendCode.visibility = View.INVISIBLE
            viewModel.resendActivation()
        }

    }

    override fun onResume() {
        super.onResume()
        setTime(300000)
        startTimer()

    }

    private fun setTime(milliseconds: Long) {
        mStartTimeInMillis = milliseconds
        resetTimer()
    }


    private fun startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis
        mCountDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                mTimerRunning = false
                txtResendCode.visibility = View.VISIBLE
            }
        }.start()
        mTimerRunning = true

    }

    private fun resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis
        updateCountDownText()
    }

    private fun updateCountDownText() {
        val hours = (mTimeLeftInMillis / 1000).toInt() / 3600
        val minutes = (mTimeLeftInMillis / 1000 % 3600).toInt() / 60
        val seconds = (mTimeLeftInMillis / 1000).toInt() % 60
        val timeLeftFormatted: String?
        timeLeftFormatted = if (hours > 0) {
            java.lang.String.format(
                Locale.getDefault(),
                "%d:%02d:%02d", hours, minutes, seconds
            )
        } else {
            java.lang.String.format(
                Locale.getDefault(),
                "%02d:%02d", minutes, seconds
            )
        }
        txtDownTimer.text = timeLeftFormatted ?: ""
    }


    override fun onStop() {
        requireActivity().finish()
        mCountDownTimer!!.cancel()
        super.onStop()
    }


}