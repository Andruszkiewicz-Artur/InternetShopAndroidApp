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
import com.andruszkiewicz.internetshop.domain.mapper.toPrize
import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import com.andruszkiewicz.internetshop.utils.GlobalUser
import com.andruszkiewicz.internetshop.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class OrderFragment : Fragment() {

    private val TAG = OrderFragment::class.java.simpleName
    private val vm: OrderViewModel by viewModels()

    @Inject
    lateinit var repository: ProductRepository

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
        initListeners()
    }

    private fun initListeners() {
        binding.buyBnt.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val user = GlobalUser.user.value

                if (user != null && user.order != null) {
                    repository.buyOrder(user.order.id).also { isSuccessful ->
                        if (isSuccessful) {
                            GlobalUser.updateUser(user.copy(order = null))
                            withContext(Dispatchers.Main) {
                                Utils.toast("Buy products!", requireContext())
                            }
                        }
                        else Log.e(TAG, "initListeners: error")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Utils.toast("No products to buy!", requireContext())
                    }
                }
            }
        }
    }

    private fun initView() {
        initAdapter()
        setUpUserObserving()
    }

    private fun initAdapter() {
        linearLayoutManager = LinearLayoutManager(context)
        binding.recycleView.layoutManager = linearLayoutManager

        adapter = OrderRecycleView(
            products = emptyList(),
            onClickChangeStateOfQuantity = { type, product ->
                vm.changeStateOfProductCount(type, product)
            }
        )

        binding.recycleView.adapter = adapter
    }


    private fun setUpUserObserving() {
        lifecycleScope.launch(Dispatchers.IO) {
            GlobalUser.user.collectLatest { user ->
                Log.d(TAG, "setUpUserObserver: ${user.toString()}")
                if (user != null) {
                    val quantities = user.order?.products ?: emptyList()
                    val totalPrize = quantities.sumOf { (it.product.prize * it.quantity).toDouble() }

                    withContext(Dispatchers.Main) {
                        adapter.updateList(quantities)
                        binding.totalPriceTv.text = "Total prize: ${totalPrize.toFloat().toPrize()}"

                        if (quantities.isEmpty()) binding.cartEmptyTv.visibility = View.VISIBLE
                        else binding.cartEmptyTv.visibility = View.GONE
                    }
                }
            }
        }
    }


}