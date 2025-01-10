package com.andruszkiewicz.internetshop.presentation.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.andruszkiewicz.internetshop.databinding.ActivityAddProductBinding
import com.andruszkiewicz.internetshop.databinding.FragmentHomeBinding
import com.andruszkiewicz.internetshop.domain.model.UserModel
import com.andruszkiewicz.internetshop.presentation.addProduct.AddProductActivity
import com.andruszkiewicz.internetshop.presentation.login.LoginActivity
import com.andruszkiewicz.internetshop.utils.GlobalUser
import com.andruszkiewicz.internetshop.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val TAG = HomeFragment::class.java.simpleName
    private val vm: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: ProductRecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onResume() {
        super.onResume()

        vm.getProducts()
    }

    private fun initView() {
        linearLayoutManager = LinearLayoutManager(context)
        binding.productsRv.layoutManager = linearLayoutManager

        adapter = ProductRecyclerView(
            items = emptyList(),
            onClickAddItem = { product ->
                val currentUser = GlobalUser.user.value
                Log.d(TAG, "ProductRecyclerView: onClickAddItem: currentUser: $currentUser")

                if (product == null) {
                    Utils.toast(
                        context = requireContext(),
                        message = "This product is already in cart!"
                    )
                } else if (currentUser == null) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    vm.addProductToOrder(product, currentUser)
                }
            }
        )

        binding.productsRv.adapter = adapter

        setUpVMObserve()
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
                        adapter.updateUser(user)
                    }
                }
            }
        }
    }

    private fun setUpVMObserve() {
        lifecycleScope.launch(Dispatchers.IO) {
            vm.products.collectLatest { products ->
                withContext(Dispatchers.Main) {
                    adapter.updateList(products)
                }
            }
        }
    }
}