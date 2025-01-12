package com.andruszkiewicz.internetshop.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import com.andruszkiewicz.internetshop.presentation.account.AccountFragment
import com.andruszkiewicz.internetshop.presentation.home.HomeFragment
import com.andruszkiewicz.internetshop.presentation.order.OrderFragment
import com.andruszkiewicz.internetshop.R
import com.andruszkiewicz.internetshop.data.datastore.PreferencesDataStoreHelper
import com.andruszkiewicz.internetshop.data.datastore.PreferencesKey
import com.andruszkiewicz.internetshop.databinding.ActivityMainBinding
import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import com.andruszkiewicz.internetshop.presentation.home.HomeViewModel
import com.andruszkiewicz.internetshop.presentation.login.LoginActivity
import com.andruszkiewicz.internetshop.utils.GlobalUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

typealias UserDataStore = PreferencesDataStoreHelper.User

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    @Inject
    lateinit var repository: ProductRepository

    private lateinit var binding: ActivityMainBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showHomeFragment()
        observeUserStatus()
        loadCurrentUser()

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

    private fun loadCurrentUser() {
        lifecycleScope.launch(Dispatchers.IO) {
            val (email, password) = UserDataStore.getEmailAndPassword(applicationContext)

            if (email != null && password != null)
                repository
                    .logInUser(email, password)
                    ?.let { user ->
                        GlobalUser.updateUser(user)
                    }
        }
    }
}