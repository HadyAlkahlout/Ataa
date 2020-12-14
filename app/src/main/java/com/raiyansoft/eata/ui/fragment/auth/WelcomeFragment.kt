package com.raiyansoft.eata.ui.fragment.auth

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.raiyansoft.eata.R
import com.raiyansoft.eata.adapter.ViewPagerAdapter
import com.raiyansoft.eata.databinding.FragmentWelcomeBinding
import kotlinx.android.synthetic.main.fragment_details.*

class WelcomeFragment : Fragment(R.layout.fragment_welcome), OnBackClickListener {

    lateinit var mBinding: FragmentWelcomeBinding
    private var positon = 0
    private var isStart = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentWelcomeBinding.bind(view)


        mBinding.viewPager2.adapter = ViewPagerAdapter()
        mBinding.viewPager2.isUserInputEnabled = false
        mBinding.  indicator.setViewPager2(mBinding.viewPager2)
        ViewCompat.setLayoutDirection(mBinding.viewPager2, ViewCompat.LAYOUT_DIRECTION_LTR)



        mBinding.btnNext.setOnClickListener {
            positon++
            mBinding.viewPager2.currentItem = positon
            if (positon == 2) {
                mBinding.btnNext.visibility = View.GONE
                mBinding.btnSkip.text = "ابدأ"
                isStart = true
            }
        }

        mBinding.btnSkip.setOnClickListener {
            positon = 2
            mBinding.viewPager2.currentItem = positon
            mBinding.btnNext.visibility = View.GONE
            mBinding.btnSkip.text = "ابدأ"
            if (isStart) {
                findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
            }
            isStart = true
        }
    }


    private var backButtonHandler: BackButtonHandlerInterface? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        backButtonHandler = activity as BackButtonHandlerInterface?
        backButtonHandler!!.addBackClickListener(this)
    }

    override fun onDetach() {
        super.onDetach()
        backButtonHandler!!.removeBackClickListener(this)
        backButtonHandler = null
    }

    override fun onBackClick(): Boolean {
        findNavController().navigateUp()
        return false
    }
}