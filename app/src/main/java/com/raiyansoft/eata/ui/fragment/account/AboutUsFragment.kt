package com.raiyansoft.eata.ui.fragment.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.raiyansoft.eata.R
import com.raiyansoft.eata.databinding.FragmentAboutUsBinding
import com.raiyansoft.eata.ui.viewmodel.AboutUsViewModel
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.fragment_about_us.*
import timber.log.Timber


class AboutUsFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[AboutUsViewModel::class.java]
    }

    private lateinit var mBinding: FragmentAboutUsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentAboutUsBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_aboutus.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val a: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
        a.reset()

        viewModel.dataAboutUsLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            if (data.status) {
                                txt_aboutus.clearAnimation()
                                txt_aboutus.startAnimation(a)
                                txt_aboutus.text = data.data.about
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

    }


}