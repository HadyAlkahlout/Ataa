package com.raiyansoft.eata.ui.fragment.cases

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import com.raiyansoft.eata.R
import com.raiyansoft.eata.adapter.ImageSliderAdapter
import com.raiyansoft.eata.databinding.FragmentDetailsBinding
import com.raiyansoft.eata.model.cases.DataX
import com.raiyansoft.eata.model.donation.PostDonate
import com.raiyansoft.eata.model.save.PostSave
import com.raiyansoft.eata.ui.fragment.dialog.DonatesDialogFragment
import com.raiyansoft.eata.ui.fragment.dialog.PaidDialogFragment
import com.raiyansoft.eata.ui.viewmodel.DetailsViewModel
import com.raiyansoft.eata.ui.viewmodel.SaveViewModel
import com.raiyansoft.eata.ui.viewmodel.TypeCaseViewModel
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Constants.DETALIS
import com.raiyansoft.eata.util.Constants.NAME
import com.raiyansoft.eata.util.Constants.PHONE_NUMBER
import com.raiyansoft.eata.util.Constants.TOKEN_MESSAGE
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.fragment_details.*
import timber.log.Timber


class DetailsFragment : Fragment() {

    private lateinit var mBinding: FragmentDetailsBinding

    private val details by lazy {
        requireArguments().getParcelable<DataX>(DETALIS)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[SaveViewModel::class.java]
    }

    private val viewModelDetails by lazy {
        ViewModelProvider(this)[DetailsViewModel::class.java]
    }

    private val viewModelCases by lazy {
        ViewModelProvider(this)[TypeCaseViewModel::class.java]
    }
    private val viewModelSave by lazy {
        ViewModelProvider(requireActivity())[SaveViewModel::class.java]
    }

    private val share by lazy {
        Constants.getSharePref(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentDetailsBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewpager()


        toolbar_details.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        txt_description.text = details!!.details
        tv_total.text = (details!!.total + " دك")

        if ((share.getInt(Constants.TYPE, 0) == 0))
            btn_donates.setBackgroundColor(requireActivity().getColor(R.color.colorPurple))
        else {
            btn_donates.setBackgroundColor(requireActivity().getColor(R.color.colorBtn))
            btn_donates.text = "تعديل"
            btn_save.text = "حذف الحالة"

            btn_save.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete, 0)
        }



        viewModel.SaveLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            if (data.code == 121) {
                                Snackbar.make(
                                    mBinding.root,
                                    "تم حفظ الحالة مسبقا",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                viewModel.getSave = null
                                viewModel.page = 1
                                viewModel.dataGeSaveLiveData.value = null
                                viewModel.getSave()
                            } else {
                                Snackbar.make(mBinding.root, "تم حفظ الحالة", Snackbar.LENGTH_SHORT)
                                    .show()
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

        Log.e("ttsad", "${details!!.images}")
        viewModelDetails.dataStatusLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            if (data.status) {
                                Snackbar.make(
                                    mBinding.root,
                                    "تم حذف الحالة",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                viewModelCases.getCease("",0)
                                findNavController().navigateUp()
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


        btn_save.setOnClickListener {
            if ((share.getInt(Constants.TYPE, 0) == 0))
                viewModel.postSave(PostSave(details!!.id.toLong()))
            else {
                viewModelDetails.deleteCase(details!!.id.toString(), 1)
            }
        }

        btn_donates.setOnClickListener {
            if ((share.getInt(Constants.TYPE, 0) == 0)) {

                val bundle = Bundle()
                bundle.putString("type", "details")
                bundle.putBoolean("donate_status", true)
                viewModelDetails.PostDonation(PostDonate(details!!.id.toString()))
                Log.e("Eee id vv",id.toString())
                bundle.putParcelable(DETALIS, details)
                findNavController().navigate(R.id.action_detailsFragment_to_messageFragment, bundle)

            }else {
                val bundle = Bundle()
                bundle.putParcelable(DETALIS, details)
                bundle.putString("type", "update")
                findNavController().navigate(
                    R.id.action_detailsFragment_to_addCaseUserFragment,
                    bundle
                )
            }
        }

    }

    private val adapterImage by lazy {
        ImageSliderAdapter(
            ArrayList()
        )
    }

    private fun setUpViewpager() {
        adapterImage.data.clear()
        adapterImage.data.addAll(details!!.images)
        view_pager.apply {
            if (adapterImage.data.size == 0) {
                view_pager.visibility = View.GONE
                indicator.visibility = View.GONE
            }
            adapter = adapterImage
            indicator.setViewPager2(view_pager)
        }
    }


}