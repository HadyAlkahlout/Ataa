package com.raiyansoft.eata.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.raiyansoft.eata.R
import com.raiyansoft.eata.adapter.CasesAdapter
import com.raiyansoft.eata.databinding.FramgnetHomeCaseBinding
import com.raiyansoft.eata.model.cases.DataX
import com.raiyansoft.eata.ui.viewmodel.TypeCaseViewModel
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.OnScrollListener
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.framgnet_home_case.*
import timber.log.Timber

class HomeCaseFragment: Fragment(), CasesAdapter.onClick {


    private lateinit var mBinding: FramgnetHomeCaseBinding
    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

    private val adapterCases by lazy {
        CasesAdapter(ArrayList(), this)
    }

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[TypeCaseViewModel::class.java]
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FramgnetHomeCaseBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        viewModel.getCease("", 1)
        viewModel.dataCaseLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            hideProgressBar()
                            Log.e("asdfasdf","${data.code}")

                            if (data.status) {
                                onScrollListener.totalCount = data.data.countTotal
                                adapterCases.data.clear()
                                adapterCases.data.addAll(data.data.data)
                                adapterCases.notifyDataSetChanged()
                                if (adapterCases.data.size == 0) {
                                    textView2.text = "لا يوجد تحديثات"
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        Timber.d("onViewCreated-> Resource.Error ${response.message}")
                        hideProgressBar()
                        Log.e("asdfasdf","${response.message}")

                    }
                    is Resource.Loading -> {
                        Timber.d("onViewCreated-> Resource.Loading")
                        showProgressBar()
                    }
                }
            })
        loadRecyclerView()

    }

    override fun onClickItem(content: DataX) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.DETALIS, content)
        parentFragment?.parentFragment?.findNavController()
            ?.navigate(R.id.action_mainFragment_to_detailsFragment, bundle)

    }

    private val onScrollListener = OnScrollListener(isLoading, isLastPage, 0) {
        viewModel.getCease("",1)
        isScrolling = false
    }

    private fun loadRecyclerView() {
        donates_list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterCases
            setHasFixedSize(true)
            layoutAnimation = AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.recyclerview_layout_animation
            )
            addOnScrollListener(onScrollListener)
        }
    }


    private fun hideProgressBar() {
        Timber.d("hideProgressBar")
        isLoading = false
    }

    private fun showProgressBar() {
        Timber.d(" showProgressBar")
        isLoading = true
    }
}