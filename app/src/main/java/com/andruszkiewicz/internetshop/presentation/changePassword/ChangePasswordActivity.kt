package com.andruszkiewicz.internetshop.presentation.changePassword

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.andruszkiewicz.internetshop.R
import com.andruszkiewicz.internetshop.databinding.ActivityChangePasswordBinding
import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import com.andruszkiewicz.internetshop.presentation.UserDataStore
import com.andruszkiewicz.internetshop.utils.GlobalUser
import com.andruszkiewicz.internetshop.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {

    private val TAG = ChangePasswordActivity::class.java.simpleName

    @Inject
    lateinit var repository: ProductRepository

    private var _binding: ActivityChangePasswordBinding? = null
    private val binding get() = _binding!!

    private var currentPassword = ""
    private var newPassword = ""
    private var newRePassword = ""
    private var userEmail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        _binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
        initData()
    }

    private fun initData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val user = GlobalUser.user.first()

            if (user != null) {
                userEmail = user.email
            } else {
                withContext(Dispatchers.Main) {
                    Utils.toast("Log in again!", applicationContext)
                    finish()
                }
            }
        }
    }

    private fun initListener() {
        binding.backBnt.setOnClickListener {
            finish()
        }

        binding.changePasswordBnt.setOnClickListener {
            validData()
        }
    }

    private fun validData() {
        currentPassword = binding.currentPasswordEt.text.toString().trim()
        newPassword = binding.newPasswordEt.text.toString().trim()
        newRePassword = binding.newRePasswordEt.text.toString().trim()

        if (currentPassword.isBlank()) {
            binding.currentPasswordEt.error = "Please enter current password"
            binding.currentPasswordEt.requestFocus()
        } else if (newPassword.isBlank()) {
            binding.newPasswordEt.error = "Please enter new password"
            binding.newPasswordEt.requestFocus()
        } else if (newRePassword.isBlank()) {
            binding.newRePasswordEt.error = "Please enter new re-password"
            binding.newRePasswordEt.requestFocus()
        } else if (newPassword != newRePassword) {
            binding.newRePasswordEt.error = "Passwords are not the same"
            binding.newRePasswordEt.requestFocus()
        } else if (newPassword == currentPassword) {
            binding.newPasswordEt.error = "New password is the same as current password"
            binding.newPasswordEt.requestFocus()
        } else {
            changePassword()
        }
    }

    private fun changePassword() {
        lifecycleScope.launch(Dispatchers.IO) {
            repository.changePassword(
                email = userEmail,
                oldPassword = currentPassword,
                newPassword = newPassword
            ).also { user ->
                Log.d(TAG, "changePassword: user: $user")
                Log.d(TAG, "changePassword: userEmail: $userEmail")
                Log.d(TAG, "changePassword: currentPassword: $currentPassword")
                Log.d(TAG, "changePassword: newPassword: $newPassword")

                if (user != null) {
                    UserDataStore.saveEmailAndPassword(
                        email = userEmail,
                        password = newPassword,
                        this@ChangePasswordActivity
                    )

                    withContext(Dispatchers.Main) {
                        Utils.toast("Password changed!", applicationContext)
                        finish()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        binding.currentPasswordEt.error = "Current password is not correct"
                        binding.currentPasswordEt.requestFocus()
                    }
                }
            }
        }
    }
}