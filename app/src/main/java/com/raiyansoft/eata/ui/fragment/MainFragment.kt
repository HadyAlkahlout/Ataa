package com.raiyansoft.eata.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.raiyansoft.eata.R
import com.raiyansoft.eata.databinding.FragmentMainBinding
import com.raiyansoft.eata.ui.activity.MainActivity
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Constants.getSharePref
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.fragment_donates.*
import timber.log.Timber

class MainFragment : Fragment() {

    private lateinit var mBinding: FragmentMainBinding
    private val shear by lazy {
        getSharePref(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentMainBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        MainActivity.setLanguage("ar", requireContext())

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val navegation =
        childFragmentManager.findFragmentById(R.id.fragment_nav_host_main)
            ?.findNavController()?.also {
                NavigationUI.setupWithNavController(
                    mBinding.bottomNav,
                    it
                )
            }
//        val nav = navegation!!.graph
        if (getSharePref(requireContext()).getInt(Constants.TYPE,0) == 0){
            mBinding.bottomNav.inflateMenu(R.menu.bottom_nav_menu)
            if (Constants.nav == 0) {
                mBinding.bottomNav.selectedItemId = mBinding.bottomNav.menu[1].itemId
                Constants.nav = 1
            }
//            nav.startDestination = R.id.homeFragment

        }else{
            mBinding.bottomNav.inflateMenu(R.menu.bottom_nav_menu_user)
            Constants.nav = 0
//            nav.startDestination = R.id.categoriesFragment
        }
//        navegation.graph = nav
    }

}