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
import com.raiyansoft.eata.model.donation.PostDonate
import com.raiyansoft.eata.ui.viewmodel.DetailsViewModel
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.dialog_add_success.view.*
import kotlinx.android.synthetic.main.dialog_done.view.*
import kotlinx.android.synthetic.main.dialog_paid.view.*
import timber.log.Timber

class AddSuccessDialogFragment(val onGo: GoFragmentMessage) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_add_success, container, false)
        dialog!!.requestWindowFeature(STYLE_NO_TITLE)
        dialog!!.setCancelable(false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners(view)


    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }


    private fun setupClickListeners(view: View) {

        view.btnOK2.setOnClickListener {
            onGo.onClickDo("", "")
            dismiss()
        }


    }

    interface GoFragmentMessage {
        fun onClickDo(code: String, total: String)
    }

}