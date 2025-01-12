package com.andruszkiewicz.internetshop.presentation.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.autofill.AutofillManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.andruszkiewicz.internetshop.data.datastore.PreferencesDataStoreHelper
import com.andruszkiewicz.internetshop.databinding.ActivityRegisterBinding
import com.andruszkiewicz.internetshop.domain.enums.UserStatus
import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import com.andruszkiewicz.internetshop.presentation.MainActivity
import com.andruszkiewicz.internetshop.utils.GlobalUser
import com.andruszkiewicz.internetshop.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

typealias UserDataStore = PreferencesDataStoreHelper.User

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val TAG = RegisterActivity::class.java.simpleName

    @Inject
    lateinit var repository: ProductRepository

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    private var mail: String = ""
    private var password: String = ""
    private var rePassword: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        binding.backBnt.setOnClickListener {
            onBackPressed()
        }
        binding.registerBnt.setOnClickListener {
            validData()
        }
        binding.logInTv.setOnClickListener {
            onBackPressed()
        }
    }

    private fun validData() {
        mail = binding.mailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()
        rePassword = binding.rePasswordEt.text.toString().trim()

        if(mail.isBlank()) {
            binding.mailEt.error = "Entered email!"
            binding.mailEt.requestFocus()
        } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            binding.mailEt.error = "Valid email!"
            binding.mailEt.requestFocus()
        } else if(password.isBlank()) {
            binding.passwordEt.error = "Entered password!"
            binding.passwordEt.requestFocus()
        } else if(rePassword.isBlank()) {
            binding.rePasswordEt.error = "Entered re-password!"
            binding.rePasswordEt.requestFocus()
        } else if(password != rePassword) {
            binding.rePasswordEt.error = "Passwords are different!"
            binding.rePasswordEt.requestFocus()
        } else {
            registerUser()
        }
    }

    private fun registerUser() {
        lifecycleScope.launch(Dispatchers.IO) {
            val user = repository.createUser(
                email = mail,
                password = password,
                UserStatus.User
            )

            withContext(Dispatchers.Main) {
                if (user != null) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        UserDataStore.saveEmailAndPassword(
                            user.email,
                            password,
                            applicationContext
                        )
                    }
                    setCredentials()

                    GlobalUser.updateUser(user)
                    finishAffinity()
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    startActivity(intent)

                    Utils.toast(
                        message = "Register success!",
                        context = this@RegisterActivity
                    )
                } else {
                    Utils.toast(
                        message = "User with that email already exists!",
                        context = this@RegisterActivity
                    )
                    binding.mailEt.error = "Use another email!"
                    binding.mailEt.requestFocus()
                }
            }
        }
    }

    private fun setCredentials() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val autofillManager =  getSystemService(AutofillManager::class.java)

            if (autofillManager != null && autofillManager.isEnabled) {
                binding.mailEt.setAutofillHints("emailAddress")
                binding.passwordEt.setAutofillHints("password")

                autofillManager.commit()
            }
        }
    }
}