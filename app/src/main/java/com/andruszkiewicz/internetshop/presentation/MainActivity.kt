package com.andruszkiewicz.internetshop.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.andruszkiewicz.internetshop.presentation.account.AccountFragment
import com.andruszkiewicz.internetshop.presentation.home.HomeFragment
import com.andruszkiewicz.internetshop.presentation.order.OrderFragment
import com.andruszkiewicz.internetshop.R
import com.andruszkiewicz.internetshop.databinding.ActivityMainBinding
import com.andruszkiewicz.internetshop.presentation.home.HomeViewModel
import com.andruszkiewicz.internetshop.presentation.login.LoginActivity
import com.andruszkiewicz.internetshop.utils.GlobalUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        observeUserStatus()

        binding.bottomNv.setOnItemSelectedListener { item ->
            val currentUser = GlobalUser.user.value

            when (item.itemId) {
                R.id.menu_home -> {
                    showHomeFragment()

                    true
                }
                R.id.menu_order -> {
                    if (currentUser == null) goToLogIn()
                    else showOrderFragment()
                    true
                }
                R.id.menu_account -> {
                    if(currentUser == null) goToLogIn()
                    else showAccountFragment()

                    true
                }
                else -> false
            }
        }
    }

    private fun goToLogIn() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
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

    private fun showAccountFragment() {
        binding.toolbarTitleRl.text = "Account"

        val fragment = AccountFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentsFl.id, fragment, "")
        fragmentTransaction.commit()
    }

    private fun observeUserStatus() {
        lifecycleScope.launch(Dispatchers.IO) {
            GlobalUser.user.collect { user ->
                if (user == null) {
                    withContext(Dispatchers.Main) {
                        showHomeFragment()
                    }
                }
            }
        }
    }
}