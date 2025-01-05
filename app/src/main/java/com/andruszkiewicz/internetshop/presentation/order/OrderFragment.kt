package com.andruszkiewicz.internetshop.presentation.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.andruszkiewicz.internetshop.databinding.FragmentOrderBinding
import com.andruszkiewicz.internetshop.databinding.ListOfProductsInCartBinding
import com.andruszkiewicz.internetshop.utils.GlobalUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class OrderFragment : Fragment() {

    private val TAG = OrderFragment::class.java.simpleName
    private val vm: OrderViewModel by viewModels()

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: OrderRecycleView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        linearLayoutManager = LinearLayoutManager(context)
        binding.recycleView.layoutManager = linearLayoutManager

        adapter = OrderRecycleView(
            products = emptyList(),
            onClickChangeStateOfQuantity = { type, product ->
                vm.changeStateOfProductCount(type, product)
            }
        )

        binding.recycleView.adapter = adapter

        setUpUserObserving()
    }

    private fun setUpUserObserving() {
        Log.d(TAG, "Start working setUpUserObserving")
        lifecycleScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Start working lifecycleScope in setUpUserObserving")
            GlobalUser.user.collectLatest { user ->
                Log.d(TAG, "setUpUserObserver: ${user.toString()}")
                if (user != null) {
                    withContext(Dispatchers.Main) {
                        adapter.updateList(user.order?.products ?: emptyList())
                    }
                }
            }
        }
    }


}