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
import com.raiyansoft.eata.databinding.FragmentPrivacyBinding
import com.raiyansoft.eata.ui.viewmodel.PrivacyViewModel
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.fragment_privacy.*
import kotlinx.android.synthetic.main.fragment_privacy.toolbar
import timber.log.Timber


class PrivacyFragment : Fragment() {


    private lateinit var mBinding: FragmentPrivacyBinding
    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[PrivacyViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentPrivacyBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        val a: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
        a.reset()




        viewModel.dataPrivacyLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->

                            txtPrivacy.clearAnimation()
                            txtPrivacy.startAnimation(a)
                            txtPrivacy.text = data.data.privacy
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