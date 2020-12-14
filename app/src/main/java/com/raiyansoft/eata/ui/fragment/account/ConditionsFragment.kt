package com.raiyansoft.eata.ui.fragment.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.raiyansoft.eata.R
import com.raiyansoft.eata.databinding.FragmentConditionsBinding
import com.raiyansoft.eata.ui.viewmodel.ConditionsViewModel
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.fragment_conditions.*
import kotlinx.android.synthetic.main.fragment_conditions.toolbar
import timber.log.Timber

class ConditionsFragment : Fragment() {

    private lateinit var mBinding: FragmentConditionsBinding
    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[ConditionsViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentConditionsBinding.inflate(inflater, container, false).apply {
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


        viewModel.dataConditionsLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            if (data.status) {
                                txtContent.clearAnimation()
                                txtContent.startAnimation(a)
                                txtContent.text = data.data.conditions
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