package com.raiyansoft.eata.ui.fragment.dialog


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.eata.R
import com.raiyansoft.eata.model.donation.ConfirmDonate
import com.raiyansoft.eata.model.donation.PostDonate
import com.raiyansoft.eata.ui.viewmodel.DetailsViewModel
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.dialog_paid.view.*
import timber.log.Timber

class PaidDialogFragment(val onGo: GoFragmentMessage, val id: String, val totalCase: String) : DialogFragment() {
    private val viewModel by lazy {
        ViewModelProvider(this)[DetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_paid, container, false)
        dialog!!.requestWindowFeature(STYLE_NO_TITLE)
        dialog!!.setCancelable(false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners(view)

        viewModel.dataPostConfirmDonatesLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            if (data.status) {
                                Constants.dialog.dismiss()
                                onGo.onClickGo("", totalCase)
                                dismiss()
                           Log.e("eeee",data.toString())
                            }
                            Log.e("eeee",data.status.toString())
                        }
                    }
                    is Resource.Error -> {
                        Constants.dialog.dismiss()
                        Log.e("eeee error paid",response.message.toString())
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


    private fun setupClickListeners(view: View) {

        view.btnYea.setOnClickListener {
            viewModel.postConfirmDonate(ConfirmDonate(id,totalCase))
            Constants.showDialog(requireActivity())
          /*  onGo.onClickGo("", totalCase)
           dismiss()*/
        }

        view.btnNo.setOnClickListener {
            dismiss()
        }

    }

    interface GoFragmentMessage {
        fun onClickGo(code: String, total: String)
    }

}