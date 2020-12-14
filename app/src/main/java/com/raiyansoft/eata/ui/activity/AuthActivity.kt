package com.raiyansoft.eata.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.raiyansoft.eata.R
import com.raiyansoft.eata.ui.fragment.auth.BackButtonHandlerInterface
import com.raiyansoft.eata.ui.fragment.auth.OnBackClickListener
import java.lang.ref.WeakReference
import java.util.*


class AuthActivity : AppCompatActivity(), BackButtonHandlerInterface {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        MainActivity.setLanguage("ar", this)


    }

    private val backClickListenersList: ArrayList<WeakReference<OnBackClickListener>> = ArrayList()

    override fun addBackClickListener(onBackClickListener: OnBackClickListener) {
        backClickListenersList.add(WeakReference(onBackClickListener))
    }

    override fun removeBackClickListener(onBackClickListener: OnBackClickListener) {
        val iterator: MutableIterator<WeakReference<OnBackClickListener>> =
            backClickListenersList.iterator()
        while (iterator.hasNext()) {
            val weakRef: WeakReference<OnBackClickListener> = iterator.next()
            if (weakRef.get() === onBackClickListener) {
                iterator.remove()
            }
        }
    }

    override fun onBackPressed() {
        if (fragmentsBackKeyIntercept()) {
            super.onBackPressed()
        }

    }

    private fun fragmentsBackKeyIntercept(): Boolean {
        var isIntercept = false
        for (weakRef in backClickListenersList) {
            val onBackClickListener: OnBackClickListener = weakRef.get()!!
            val isFragmIntercept = onBackClickListener.onBackClick()
            if (!isIntercept) isIntercept = isFragmIntercept
        }
        return isIntercept
    }
}