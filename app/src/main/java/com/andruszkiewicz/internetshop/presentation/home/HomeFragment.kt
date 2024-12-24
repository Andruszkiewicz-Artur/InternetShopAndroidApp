package com.andruszkiewicz.internetshop.presentation.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.andruszkiewicz.internetshop.R
import com.andruszkiewicz.internetshop.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val vm: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        Log.d("HomeFragment", "HomeViewModel is initialized")

        vm.showLog()
        // Inflate the layout for this fragment
        return binding.root
    }
}