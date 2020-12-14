package com.raiyansoft.eata.ui.fragment.dialog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.raiyansoft.eata.R
import kotlinx.android.synthetic.main.dialog_problem.view.*

class ProblemDialogFragment(val onGo: ProblemFragmentMessage) : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_problem, container, false)
        dialog!!.requestWindowFeature(STYLE_NO_TITLE)
        dialog!!.setCancelable(false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners(view)
        requireDialog().setCancelable(false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }


    private fun setupClickListeners(view: View) {

        view.btnProblem.setOnClickListener {
            onGo.onClickProblem()
        }

    }

    interface ProblemFragmentMessage {
        fun onClickProblem()
    }

}