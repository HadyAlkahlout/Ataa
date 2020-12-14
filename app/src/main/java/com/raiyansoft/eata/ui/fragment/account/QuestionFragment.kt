package com.raiyansoft.eata.ui.fragment.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.raiyansoft.eata.R
import com.raiyansoft.eata.adapter.QuestionAdapter
import com.raiyansoft.eata.databinding.FragmentQuestionBinding
import com.raiyansoft.eata.ui.viewmodel.QuestionViewModel
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.fragment_question.*
import kotlinx.android.synthetic.main.fragment_question.toolbar
import timber.log.Timber


class QuestionFragment : Fragment() {

    private lateinit var mBinding: FragmentQuestionBinding


    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[QuestionViewModel::class.java]
    }
    private val adapterQuestion by lazy {
        QuestionAdapter(ArrayList())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentQuestionBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.dataQuestionLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            if (data.status) {
                                adapterQuestion.data.clear()
                                adapterQuestion.data.addAll(data.data.data)
                                adapterQuestion.notifyDataSetChanged()
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



        loadRecyclerView()
    }


    private fun loadRecyclerView() {
        rcDataQuestion.apply {
            adapter = adapterQuestion
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            layoutAnimation = AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.recyclerview_layout_animation
            )

        }
    }

}