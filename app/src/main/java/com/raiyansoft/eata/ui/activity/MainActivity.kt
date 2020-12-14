package com.raiyansoft.eata.ui.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.raiyansoft.eata.databinding.ActivityMainBinding
import com.raiyansoft.eata.util.Constants.getSharePref
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val share by lazy {
        getSharePref(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }


    companion object {
        fun setLanguage(lan: String, context: Context) {
            val res = context.resources
            val dr = res.displayMetrics
            val cr = res.configuration
            cr.setLocale(Locale(lan))
            res.updateConfiguration(cr, dr)
        }
    }


}