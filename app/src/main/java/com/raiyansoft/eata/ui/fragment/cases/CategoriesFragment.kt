package com.raiyansoft.eata.ui.fragment.cases

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.raiyansoft.eata.R
import com.raiyansoft.eata.adapter.CasesAdapter
import com.raiyansoft.eata.adapter.CasesNeedAdapter
import com.raiyansoft.eata.adapter.CategoriesAdapter
import com.raiyansoft.eata.databinding.FragmentCategoriesBinding
import com.raiyansoft.eata.model.cases.DataX
import com.raiyansoft.eata.model.categories.Content
import com.raiyansoft.eata.ui.viewmodel.CategoriesViewModel
import com.raiyansoft.eata.ui.viewmodel.TypeCaseViewModel
import com.raiyansoft.eata.util.Constants.DETALIS
import com.raiyansoft.eata.util.Constants.TYPE
import com.raiyansoft.eata.util.Constants.TYPE_CASES
import com.raiyansoft.eata.util.Constants.getSharePref
import com.raiyansoft.eata.util.OnScrollListener
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.fragment_categories.*
import timber.log.Timber

class CategoriesFragment : Fragment(), CategoriesAdapter.OnClickCategories,
    CasesNeedAdapter.onClick {

    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false


    private lateinit var mBinding: FragmentCategoriesBinding
    private val adapterCategories by lazy {
        CategoriesAdapter(ArrayList(), this)
    }
    private val adapterMyCase by lazy {
        CasesNeedAdapter(ArrayList(), this)
    }
    private val share by lazy {
        getSharePref(requireContext())
    }

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[CategoriesViewModel::class.java]
    }


    private val viewModelCases by lazy {
        ViewModelProvider(requireActivity())[TypeCaseViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCategoriesBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("dadfasd", "onViewCreated")
        getData()

        loadRecyclerView()




        btnAddCase.setOnClickListener {

            refrashData()
            navController(R.id.action_mainFragment_to_addCaseUserFragment, Bundle())
        }

        fabAddCase.setOnClickListener {
            refrashData()
            navController(R.id.action_mainFragment_to_addCaseUserFragment, Bundle())

        }

    }

    private val onScrollListener = OnScrollListener(isLoading, isLastPage, 0) {
        if (share.getInt(TYPE, 0) == 0)
            viewModel.getCategories()
        else {
            viewModelCases.getCease("", 0)
        }
        isScrolling = false
    }

    private fun loadRecyclerView() {
        rcDataCategories.apply {
            adapter =
                if (share.getInt(TYPE, 0) == 0)
                    adapterCategories
                else adapterMyCase
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            layoutAnimation = AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.recyclerview_layout_animation
            )
            addOnScrollListener(onScrollListener)
        }
    }

    override fun onClick(category: Content) {

        Bundle().apply {
            this.putParcelable(TYPE_CASES, category)
        }.also {
            parentFragment?.parentFragment?.findNavController()
                ?.navigate(R.id.action_mainFragment_to_typeCasesFragment, it)
        }

    }


    private fun getData() {

        if (share.getInt(TYPE, 0) == 0) {
            Log.e("asdfas", "getData")
            viewModel.getCategories()
            viewModel.dataCategoriesLiveData.observe(viewLifecycleOwner, Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        hideProgressBar()
                        response.data?.let { data ->
                            if (data.status) {
                                onScrollListener.totalCount = 10
                                adapterCategories.data.clear()
                                adapterCategories.data.addAll(data.data.data)
                                adapterCategories.notifyDataSetChanged()
                            }
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()

                        response.message?.let { message ->

                        }
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            })

            fabAddCase.visibility = View.GONE
            btnAddCase.visibility = View.GONE
            textView2.visibility = View.GONE

        } else {

            viewModelCases.getCease("", 0)
            viewModelCases.dataCaseLiveData.observe(viewLifecycleOwner, Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        hideProgressBar()
                        response.data?.let { data ->
                            if (data.status) {
                                onScrollListener.totalCount = data.data.countTotal
                                adapterMyCase.data.clear()
                                adapterMyCase.data.addAll(data.data.data)
                                adapterMyCase.notifyDataSetChanged()

                                if (adapterMyCase.itemCount > 0) {
                                    fabAddCase.visibility = View.VISIBLE
                                    btnAddCase.visibility = View.GONE
                                    textView2.visibility = View.GONE

                                } else {
                                    fabAddCase.visibility = View.GONE
                                    btnAddCase.visibility = View.VISIBLE
                                    textView2.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()

                        response.message?.let { message ->

                        }
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            })
        }

    }


    private fun navController(id: Int, bundle: Bundle) {
        parentFragment?.parentFragment?.findNavController()
            ?.navigate(id, bundle)
    }


    private fun hideProgressBar() {
        Timber.d("hideProgressBar")
        isLoading = false
    }

    private fun showProgressBar() {
        Timber.d(" showProgressBar")
        isLoading = true
    }


    override fun onClickItem(content: DataX) {
        val bundle = Bundle()
        bundle.putParcelable(DETALIS, content)
        refrashData()
        parentFragment?.parentFragment?.findNavController()
            ?.navigate(R.id.action_mainFragment_to_detailsFragment, bundle)
    }

    private fun refrashData() {
        viewModelCases.page = 1
        viewModelCases.case = null
        viewModelCases.dataCaseLiveData.value = null
        adapterMyCase.data.clear()
    }
}