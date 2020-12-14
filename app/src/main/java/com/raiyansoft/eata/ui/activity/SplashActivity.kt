package com.raiyansoft.eata.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.raiyansoft.eata.R
import com.raiyansoft.eata.databinding.ActivitySplashBinding
import com.raiyansoft.eata.ui.fragment.dialog.PaidDialogFragment
import com.raiyansoft.eata.ui.fragment.dialog.ProblemDialogFragment
import com.raiyansoft.eata.ui.fragment.dialog.UpdateDialogFragment
import com.raiyansoft.eata.ui.viewmodel.SplashState
import com.raiyansoft.eata.ui.viewmodel.SplashViewModel
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.activity_splash.*
import timber.log.Timber

class SplashActivity : AppCompatActivity(), UpdateDialogFragment.GoFragmentMessage ,ProblemDialogFragment.ProblemFragmentMessage{

    private lateinit var binding: ActivitySplashBinding


    val viewModel by lazy {
        ViewModelProvider(this)[SplashViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MainActivity.setLanguage("ar", this)


        val pInfo = packageManager.getPackageInfo(packageName, 0)
        val version = pInfo.versionCode

        viewModel.liveData.observe(this, Observer {
            when (it) {
                is SplashState.MainActivity -> {
                    viewModel.dataStingLiveData.observe(
                            this,
                            androidx.lifecycle.Observer { response ->
                                Timber.d(" onViewCreated->viewModel")
                                when (response) {
                                    is Resource.Success -> {
                                        Timber.d(" onViewCreated->Resource.Success")
                                        response.data?.let { data ->
                                            if (data.status) {
                                                if ((data.data.forceUpdate == "yes" || data.data.forceUpdate == "android")
                                                        && data.data.android_version != version.toString()
                                                ) {
                                                    UpdateDialogFragment(
                                                            this
                                                    ).show(supportFragmentManager, "")
                                                } else if (data.data.forceClose == "yes" || data.data.forceClose == "android") {
                                                    ProblemDialogFragment(
                                                            this
                                                    ).show(supportFragmentManager, "")
                                                } else if ((data.data.forceUpdate == "no" && data.data.forceClose == "no")
                                                        && data.data.android_version == version.toString()
                                                ) {
                                                    goToMainActivity()
                                                }
                                            }
                                        }
                                    }
                                    is Resource.Error -> {
                                        Log.e("hdhd", "onCreate: ${response.message}")
                                    }
                                    is Resource.Loading -> {
                                        Timber.d("onViewCreated-> Resource.Loading")
                                    }
                                }
                            })
                }
            }
        })

        val a: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        a.reset()

        imageLogo.clearAnimation()
        imageLogo.startAnimation(a)


    }


    private fun goToMainActivity() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }


    override fun onClickGo() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }

    override fun onClickProblem() {
        finish()

    }
}