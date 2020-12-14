package com.raiyansoft.eata.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.eata.R
import com.raiyansoft.eata.adapter.CasesAdapter
import com.raiyansoft.eata.databinding.FragmentSavingBinding
import com.raiyansoft.eata.model.cases.DataX
import com.raiyansoft.eata.model.save.PostSave
import com.raiyansoft.eata.ui.viewmodel.SaveViewModel
import com.raiyansoft.eata.util.OnScrollListener
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.fragment_saving.*
import timber.log.Timber


class SavingFragment : Fragment(), CasesAdapter.onClick {

    private lateinit var mBinding: FragmentSavingBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[SaveViewModel::class.java]
    }
    private val adapterCases by lazy {
        CasesAdapter(ArrayList(), this)
    }

    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false
    private var isDeleted = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSavingBinding.inflate(inflater)

        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadRecyclerView()

        viewModel.getSave()

        viewModel.dataGeSaveLiveData.observe(
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
                                    txtSaveDate.visibility = View.VISIBLE
                                } else {
                                    txtSaveDate.visibility = View.GONE

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


        viewModel.dataStatusLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            hideProgressBar()
                            if (data.status) {
                                if (isDeleted) {
                                    Snackbar.make(
                                        mBinding.root,
                                        "تم الحذف بنجاح",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                    isDeleted = false
                                }
                                if (adapterCases.data.size == 0) {
                                    txtSaveDate.visibility = View.VISIBLE
                                } else {
                                    txtSaveDate.visibility = View.GONE

                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        Timber.d("onViewCreated-> Resource.Error ${response.message}")
                        hideProgressBar()
                        Log.e("ttttt", "${response.message}")

                    }
                    is Resource.Loading -> {
                        Timber.d("onViewCreated-> Resource.Loading")
                        showProgressBar()
                    }
                }
            })
    }


    private val onScrollListener = OnScrollListener(isLoading, isLastPage, 0) {
        viewModel.getSave()
        isScrolling = false
    }

    private fun loadRecyclerView() {
        rcDataSave.apply {
            adapter = adapterCases
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            layoutAnimation = AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.recyclerview_layout_animation
            )
            addOnScrollListener(onScrollListener)

            val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
                ): Boolean {

                    return true // true if moved, false otherwise
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {

                    viewModel.deleteCase(PostSave(adapterCases.data[viewHolder.adapterPosition].id.toLong()))
                        .also {
                            viewModel.getSave = null
                            viewModel.page = 1
                            viewModel.dataGeSaveLiveData.value = null
                            viewModel.getSave()
                            isDeleted = true
                            adapterCases.notifyItemRemoved(swipeDir)
//                            findNavController().navigateUp()
                        }

                }
            }
            val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
            itemTouchHelper.attachToRecyclerView(this)

        }
    }

    override fun onClickItem(content: DataX) {

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