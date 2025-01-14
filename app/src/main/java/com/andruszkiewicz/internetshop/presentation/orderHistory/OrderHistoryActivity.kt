package com.andruszkiewicz.internetshop.presentation.orderHistory

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.andruszkiewicz.internetshop.R
import com.andruszkiewicz.internetshop.databinding.ActivityOrderHistoryBinding
import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import com.andruszkiewicz.internetshop.presentation.order.OrderRecycleView
import com.andruszkiewicz.internetshop.utils.GlobalUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OrderHistoryActivity : AppCompatActivity() {

    private val TAG = OrderHistoryActivity::class.java.simpleName

    @Inject
    lateinit var repository: ProductRepository

    private var _binding: ActivityOrderHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: HistoryOrderRecycleView

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        _binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initListeners()
    }

    private fun initListeners() {
        binding.backBnt.setOnClickListener {
            finish()
        }
    }

    private fun initView() {
        initAdapter()
        takeUserOrders()
    }

    private fun takeUserOrders() {
        lifecycleScope.launch(Dispatchers.IO) {
            GlobalUser.user.first()?.let { user ->
                repository
                    .getOrders(user.email)
                    .also { orders ->
                        lifecycleScope.launch(Dispatchers.Main) {
                            adapter.updateList(orders)
                            if (orders != null) binding.noOrdersTv.visibility = View.GONE
                            else binding.noOrdersTv.visibility = View.VISIBLE
                        }
                    }
            }
        }
    }

    private fun initAdapter() {
        linearLayoutManager = LinearLayoutManager(this)
        binding.ordersRv.layoutManager = linearLayoutManager

        adapter = HistoryOrderRecycleView(emptyList())

        binding.ordersRv.adapter = adapter
    }
}