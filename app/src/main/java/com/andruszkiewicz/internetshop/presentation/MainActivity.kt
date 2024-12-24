package com.andruszkiewicz.internetshop.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.andruszkiewicz.internetshop.presentation.account.AccountFragment
import com.andruszkiewicz.internetshop.presentation.history.HistoryFragment
import com.andruszkiewicz.internetshop.presentation.home.HomeFragment
import com.andruszkiewicz.internetshop.presentation.order.OrderFragment
import com.andruszkiewicz.internetshop.R
import com.andruszkiewicz.internetshop.databinding.ActivityMainBinding
import com.andruszkiewicz.internetshop.presentation.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showHomeFragment()

        binding.bottomNv.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.menu_home -> {
                    showHomeFragment()

                    true
                }
                R.id.menu_order -> {
                    showOrderFragment()

                    true
                }
                R.id.menu_history_orders -> {
                    showHistoryFragment()

                    true
                }
                R.id.menu_account -> {
                    showAccountFragment()

                    true
                }
                else -> false
            }
        }
    }

    private fun showHomeFragment() {
        binding.toolbarTitleRl.text = "Home"

        val fragment = HomeFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentsFl.id, fragment, "")
        fragmentTransaction.commit()
    }

    private fun showOrderFragment() {
        binding.toolbarTitleRl.text = "Order"

        val fragment = OrderFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentsFl.id, fragment, "")
        fragmentTransaction.commit()
    }

    private fun showHistoryFragment() {
        binding.toolbarTitleRl.text = "History"

        val fragment = HistoryFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentsFl.id, fragment, "")
        fragmentTransaction.commit()

    }

    private fun showAccountFragment() {
        binding.toolbarTitleRl.text = "Account"

        val fragment = AccountFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentsFl.id, fragment, "")
        fragmentTransaction.commit()
    }
}