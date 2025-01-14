package com.andruszkiewicz.internetshop.presentation.addUser

import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.andruszkiewicz.internetshop.R
import com.andruszkiewicz.internetshop.databinding.ActivityAddUserBinding
import com.andruszkiewicz.internetshop.domain.enums.UserStatus
import com.andruszkiewicz.internetshop.domain.model.UserModel
import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import com.andruszkiewicz.internetshop.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class AddUserActivity : AppCompatActivity() {

    @Inject
    lateinit var repository: ProductRepository

    private val TAG = AddUserActivity::class.java.simpleName

    private var _binding: ActivityAddUserBinding? = null
    private val binding get() = _binding!!

    private var email = ""
    private var password = ""
    private var isAdmin = false
    private var users: List<UserModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        getUsers()
        initView()
        initListener()
    }

    private fun initView() {
        binding.userRb.isChecked = true
    }

    private fun initListener() {
        binding.backBnt.setOnClickListener {
            onBackPressed()
        }

        binding.saveUserBnt.setOnClickListener {
            createUser()
        }
    }

    private fun createUser() {
        email = binding.mailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()
        isAdmin = binding.adminRb.isChecked

        if (email.isBlank()) {
            binding.mailEt.error = "Enter email"
            binding.mailEt.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.mailEt.error = "Valid email"
            binding.mailEt.requestFocus()
        }else if (password.isBlank()) {
            binding.passwordEt.error = "Enter password"
            binding.passwordEt.requestFocus()
        } else if (users.find { it.email == email } != null) {
            binding.mailEt.error = "That email already exist"
            binding.mailEt.requestFocus()
        } else {
            saveUser()
        }
    }

//    private fun getUsers() {
//        lifecycleScope.launch(Dispatchers.IO) {
//            users = repository.getUsers()
//        }
//    }

    private fun saveUser() {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = repository.createUser(
                email = email,
                password = password,
                status = if (isAdmin) UserStatus.Admin else UserStatus.User
            )

            withContext(Dispatchers.Main) {
                if (response != null) {
                    onBackPressed()
                    Utils.toast(
                        message = "User added!",
                        context = applicationContext
                    )
                }
            }
        }
    }
}