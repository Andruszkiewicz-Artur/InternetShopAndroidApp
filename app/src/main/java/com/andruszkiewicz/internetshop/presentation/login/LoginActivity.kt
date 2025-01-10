package com.andruszkiewicz.internetshop.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.andruszkiewicz.internetshop.R
import com.andruszkiewicz.internetshop.databinding.ActivityLoginBinding
import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import com.andruszkiewicz.internetshop.presentation.MainActivity
import com.andruszkiewicz.internetshop.presentation.register.RegisterActivity
import com.andruszkiewicz.internetshop.utils.GlobalUser
import com.andruszkiewicz.internetshop.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val TAG = LoginActivity::class.java.simpleName

    @Inject
    lateinit var repository: ProductRepository

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private var email: String = ""
    private var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
    }

    private fun initListener() {
        binding.backBnt.setOnClickListener {
            onBackPressed()
        }

        binding.logInBnt.setOnClickListener {
            validData()
        }

        binding.registerTv.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validData() {
        email = binding.mailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        if (email.isBlank()) {
            binding.mailEt.error = "Entered mail!"
            binding.mailEt.requestFocus()
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.mailEt.error = "Invalid mail!"
            binding.mailEt.requestFocus()
        } else if (password.isBlank()) {
            binding.passwordEt.error = "Entered password!"
            binding.passwordEt.requestFocus()
        } else {
            logIn()
        }
    }

    private fun logIn() {
        lifecycleScope.launch(Dispatchers.IO) {
            val user = repository.logInUser(email, password)

            Log.d(TAG, "logIn: user: $user")

            if (user != null) {
                GlobalUser.updateUser(user)

                withContext(Dispatchers.Main) {
                    finishAffinity()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)

                    Utils.toast(
                        message = "Log in!",
                        context = applicationContext
                    )
                }
            } else {
                withContext(Dispatchers.Main) {
                    Utils.toast(
                        message = "Problem with log in!",
                        context = applicationContext
                    )

                    binding.mailEt.error = "Data are incorrect!"
                    binding.mailEt.requestFocus()
                }
            }
        }
    }
}