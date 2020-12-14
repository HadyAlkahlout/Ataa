package com.raiyansoft.eata.ui.fragment.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.raiyansoft.eata.R
import com.raiyansoft.eata.model.donation.ConfirmDonate
import com.raiyansoft.eata.model.donation.PostDonate
import com.raiyansoft.eata.ui.viewmodel.DetailsViewModel
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.dialog_sure_donate.view.*
import timber.log.Timber

class DonatesDialogFragment(val onGo: GoFragmentMessage, val id: String, val totalCase: String) :
    DialogFragment(),
    PaidDialogFragment.GoFragmentMessage {


    private val viewModel by lazy {
        ViewModelProvider(this)[DetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_sure_donate, container, false)
        dialog!!.requestWindowFeature(STYLE_NO_TITLE)
        dialog!!.setCancelable(false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupClickListeners(view)

        LayoutInflater.from(requireContext()).inflate(R.layout.dialog_update, null, false)

        viewModel.dataPostDonatesLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            if (data.status) {
                                onGo.onClick(data.code.toString(), total.toString())
                            }
                        }
                    }
                    is Resource.Error -> {
                        Log.e("ttttttTow", "${response.message}")

                    }
                    is Resource.Loading -> {
                        Timber.d("onViewCreated-> Resource.Loading")
                    }
                }
            })


    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupView(view: View) {
    }

    var total = totalCase.toInt()
    private fun setupClickListeners(view: View) {
        view.txtQuantity.text = "$total دك"
        view.btnAdd.setOnClickListener {
            if (total < totalCase.toInt())
                total += 10
            view.txtQuantity.text = "$total دك"
        }
        view.btnMinas.setOnClickListener {
            if (total > 100) {
                total -= 10
                view.txtQuantity.text = "$total دك"
            }
        }
        view.btnYea.setOnClickListener {
            //viewModel.PostDonation(PostDonate(id))
            val price = view.txtQuantity.text.toString().split(" ")
            onGo.onClick("", price[0])
            Log.e("eee price",  price[0])
           dismiss()
        }

        view.btnNo.setOnClickListener {
            dismiss()
        }
    }

    override fun onClickGo(code: String, total: String) {
        onGo.onClick(code, total)
    }


    interface GoFragmentMessage {
        fun onClick(code: String, total: String)
    }

}