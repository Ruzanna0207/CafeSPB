package com.cafe.cafespb.presentation

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cafe.cafespb.R
import com.cafe.cafespb.databinding.ActivityMainBinding

private var currentFragment: Fragment? = null
private var currentItem: MenuItem? = null

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentItem = binding.bottomNavigationView.menu.findItem(R.id.home)
        currentItem?.isChecked = true

        currentFragment = MainPageFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, currentFragment!!)
            .commit()

        requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //фиксированная портретная ориентация экрана
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, MainPageFragment.newInstance())
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

        //обработка условий если нажат элемент bottom.nav.view
        currentItem?.isChecked = when (currentFragment) {
            is MainPageFragment -> {
                binding.bottomNavigationView.menu.findItem(R.id.home).isChecked = true
                true
            }

            is ShopBucketFragment -> {
                binding.bottomNavigationView.menu.findItem(R.id.history).isChecked = true
                true
            }

            else -> false
        }
    }
}
