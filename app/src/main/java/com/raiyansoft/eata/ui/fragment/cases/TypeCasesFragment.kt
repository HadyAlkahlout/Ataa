package com.raiyansoft.eata.ui.fragment.cases

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
import com.raiyansoft.eata.adapter.CasesAdapter
import com.raiyansoft.eata.databinding.FragmentTypeCasesBinding
import com.raiyansoft.eata.model.cases.DataX
import com.raiyansoft.eata.model.categories.Content
import com.raiyansoft.eata.ui.viewmodel.TypeCaseViewModel
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Constants.TYPE_CASES
import com.raiyansoft.eata.util.OnScrollListener
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.fragment_type_cases.*
import kotlinx.android.synthetic.main.fragment_type_cases.toolbar
import timber.log.Timber

class TypeCasesFragment : Fragment(), CasesAdapter.onClick {

    private lateinit var mBinding: FragmentTypeCasesBinding
    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false


    private val adapterCases by lazy {
        CasesAdapter(ArrayList(), this)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[TypeCaseViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentTypeCasesBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }

    private val category by lazy {
        requireArguments().getParcelable<Content>(TYPE_CASES)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadRecyclerView()

        title.text = "${category!!.title}"

        toolbar.setNavigationOnClickListener {
           requireActivity().onBackPressed()
        }


        viewModel.getCease(category!!.id.toString(), 0)
        viewModel.dataCaseLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            hideProgressBar()
                            if (data.status) {
                                onScrollListener.totalCount = data.data.countTotal
                                adapterCases.data.clear()
                                adapterCases.data.addAll(data.data.data)
                                adapterCases.notifyDataSetChanged()
                                if (adapterCases.data.size == 0) {
                                    txtTypeCase.text = "لا يوجد ${category!!.title}"
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        Timber.d("onViewCreated-> Resource.Error ${response.message}")
                        hideProgressBar()

                    }
                    is Resource.Loading -> {
                        Timber.d("onViewCreated-> Resource.Loading")
                        showProgressBar()
                    }
                }
            })


    }

    override fun onClickItem(content: DataX) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.DETALIS, content)
        findNavController()
            .navigate(R.id.action_typeCasesFragment_to_detailsFragment, bundle)

    }

    private val onScrollListener = OnScrollListener(isLoading, isLastPage, 0) {
        viewModel.getCease(category!!.id.toString(), 0)
        isScrolling = false
    }

    private fun loadRecyclerView() {
        rcDataType.apply {
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