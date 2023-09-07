package com.cafe.cafespb.presentation

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cafe.cafespb.R
import com.cafe.cafespb.databinding.ActivityMainBinding
import com.cafe.cafespb.presentation.main_page.MainPageFragment
import com.cafe.cafespb.presentation.shop_bucket.ShopBucketFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mainPageFragment: MainPageFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, MainPageFragment.newInstance())
            .commit()

        requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //фиксированная портретная ориентация экрана
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    if (mainPageFragment == null) {
                        mainPageFragment = MainPageFragment.newInstance()
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, mainPageFragment!!)
                        .addToBackStack(null)
                        .commit()
                }

                R.id.history -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, ShopBucketFragment.newInstance4())
                        .addToBackStack(null)
                        .commit()
                }
            }
            true
        }
    }
}
