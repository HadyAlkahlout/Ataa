package com.raiyansoft.eata.ui.fragment.account

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.raiyansoft.eata.R
import com.raiyansoft.eata.adapter.DonatesAdapter
import com.raiyansoft.eata.databinding.FragmentDonatesBinding
import com.raiyansoft.eata.model.donation.Content
import com.raiyansoft.eata.ui.viewmodel.DonatesViewModel
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Constants.getSharePref
import com.raiyansoft.eata.util.OnScrollListener
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.fragment_donates.*
import timber.log.Timber


class DonatesFragment : Fragment(), DonatesAdapter.onClick {


    private lateinit var mBinding: FragmentDonatesBinding

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[DonatesViewModel::class.java]
    }
    private val adapterDonates by lazy {
        DonatesAdapter(ArrayList(), this)
    }

    private val share by lazy {
        getSharePref(requireContext())
    }
    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

    val bundlearg by lazy { arguments }

    lateinit var root: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentDonatesBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_donates.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        loadRecyclerView()



        if (bundlearg != null) {
            if ((share.getInt(Constants.TYPE, 0) == 0)) {
                toolbar_donates.visibility = View.VISIBLE
                imageView3.visibility = View.INVISIBLE
            } else {
                toolbar_donates.visibility = View.INVISIBLE
                imageView3.visibility = View.VISIBLE
            }
        }


        viewModel.dataDonatesLiveData.observe(
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
                                adapterDonates.data.clear()
                                adapterDonates.data.clear()
                                data.data.data.forEach {
                                    if (it.status == true)
                                        adapterDonates.data.add(it)
                                }
                                // adapterDonates.data.addAll(data.data.data)
                                adapterDonates.notifyDataSetChanged()
                                if (data.data.data.size == 0) {
                                    textView2.visibility = View.VISIBLE
                                } else {
                                    textView2.visibility = View.GONE

                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        Timber.d("onViewCreated-> Resource.Error ${response.message}")
                        hideProgressBar()

                    }
                    is Resource.Loading -> {
                        showProgressBar()

                        Timber.d("onViewCreated-> Resource.Loading")
                    }
                }
            })

    }

    override fun onClickItem(content: Content, type: Int) {
        Log.e("ttttttttfasd", "${type}")
        when (type) {

            1 -> {
                val bundle = Bundle()
                bundle.putString("type", "donates")
                bundle.putBoolean("donate_status", false)
                bundle.putParcelable(Constants.DONATES, content)
                if ((share.getInt(Constants.TYPE, 0) == 0))
                    if (bundlearg != null)
                        findNavController()
                            .navigate(R.id.action_donatesFragment_to_messageFragment, bundle)
                    else
                        parentFragment?.parentFragment?.findNavController()
                            ?.navigate(R.id.action_mainFragment_to_messageFragment, bundle)
                else
                    parentFragment?.parentFragment?.findNavController()
                        ?.navigate(R.id.action_mainFragment_to_messageFragment, bundle)
            }
        }

    }

    private val onScrollListener = OnScrollListener(isLoading, isLastPage, 0) {
        viewModel.getDonation()
        isScrolling = false
    }

    private fun loadRecyclerView() {
        donates_list.apply {
            adapter = adapterDonates
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