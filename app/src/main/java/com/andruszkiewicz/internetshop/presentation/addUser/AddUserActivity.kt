package com.andruszkiewicz.internetshop.presentation.addUser

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.andruszkiewicz.internetshop.R
import com.andruszkiewicz.internetshop.databinding.ActivityAddUserBinding
import com.andruszkiewicz.internetshop.domain.enums.UserStatus
import com.andruszkiewicz.internetshop.domain.model.UserEmailAndStatusModel
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
    private var user: UserEmailAndStatusModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initListener()
    }

    private fun initView() {
        binding.userRb.isChecked = true
        takeData()
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
        }else if (password.isBlank() && user == null) {
            binding.passwordEt.error = "Enter password"
            binding.passwordEt.requestFocus()
        } else {
            saveUser()
        }
    }

    private fun saveUser() {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = if (user != null) {
                repository.updateUser(
                    email = user!!.email,
                    password = password,
                    status = if (isAdmin) UserStatus.Admin else UserStatus.User
                )
            } else {
                repository.createUser(
                    email = email,
                    password = password,
                    status = if (isAdmin) UserStatus.Admin else UserStatus.User
                )
            }
            withContext(Dispatchers.Main) {
                if (response != null) {
                    Utils.toast(
                        message = if (user != null) "User edit!" else "User added!",
                        context = applicationContext
                    )
                    val resultIntent = intent
                    val userWithEmailAndStatus = response.toUserEmailAndStatus()
                    resultIntent.putExtra(Utils.USER_EXTRA, userWithEmailAndStatus)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                } else if (user == null) {
                    binding.mailEt.error = "User with that email already exist!"
                    binding.mailEt.requestFocus()
                } else {
                    Utils.toast(
                        message = "Problem with adding user!",
                        context = applicationContext
                    )
                    return@withContext
                }
            }
        }
    }

    private fun takeData() {
        user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Utils.USER_EXTRA, UserEmailAndStatusModel::class.java)
        } else intent.getParcelableExtra(Utils.USER_EXTRA)

        setUpView()
    }

    private fun setUpView() {
        with(binding) {
            if (user != null) {
                addUserTv.text = "Edit User"
                saveUserBnt.text = "Edit User"
                mailEt.isEnabled = false
                mailEt.setText(user!!.email)
                if (user!!.status == UserStatus.Admin) adminRb.isChecked = true
                else userRb.isChecked = true
            }
        }
    }
}