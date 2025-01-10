package com.andruszkiewicz.internetshop.presentation.userController

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.andruszkiewicz.internetshop.R
import com.andruszkiewicz.internetshop.databinding.ActivityUserControllerBinding

class UserControllerActivity : AppCompatActivity() {

    private val TAG = UserControllerActivity::class.java.simpleName

    private var _binding: ActivityUserControllerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        _binding = ActivityUserControllerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
        initView()
    }

    private fun initView() {
        TODO("Not yet implemented")
    }

    private fun initListener() {
        TODO("Not yet implemented")
    }
}