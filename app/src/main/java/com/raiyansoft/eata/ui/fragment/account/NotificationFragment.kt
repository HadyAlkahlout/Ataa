package com.raiyansoft.eata.ui.fragment.account

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.raiyansoft.eata.R
import com.raiyansoft.eata.adapter.NotificationAdapter
import com.raiyansoft.eata.databinding.FragmentNotificationBinding
import com.raiyansoft.eata.model.notification.Content
import com.raiyansoft.eata.ui.viewmodel.NotificationViewModel
import com.raiyansoft.eata.util.OnScrollListener
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.fragment_notification.toolbar
import timber.log.Timber


class NotificationFragment : Fragment(), NotificationAdapter.onClick {

    private lateinit var mBinding: FragmentNotificationBinding
    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[NotificationViewModel::class.java]
    }

    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false


    private val adapterNotification by lazy {
        NotificationAdapter(
            ArrayList(), this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentNotificationBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        loadRecyclerView()
        viewModel.getNotification()

        viewModel.dataNotificationLiveData.observe(
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
                                adapterNotification.data.clear()
                                adapterNotification.data.addAll(data.data.data)
                                adapterNotification.notifyDataSetChanged()
                            }
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()

                    }
                    is Resource.Loading -> {
                        Timber.d("onViewCreated-> Resource.Loading")
                        showProgressBar()

                    }
                }
            })




    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.read_all, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private val onScrollListener = OnScrollListener(isLoading, isLastPage, 0) {
        viewModel.getNotification()
        isScrolling = false
    }


    private fun loadRecyclerView() {
        notification_list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterNotification
            setHasFixedSize(true)
            layoutAnimation = AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.recyclerview_layout_animation
            )
            addOnScrollListener(onScrollListener)

        }
    }

    override fun onClickItem(
        notificaiton: Content,
        type: Int
    ) {

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