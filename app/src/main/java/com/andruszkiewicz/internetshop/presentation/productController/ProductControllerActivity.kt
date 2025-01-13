package com.andruszkiewicz.internetshop.presentation.productController

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.andruszkiewicz.internetshop.databinding.ActivityProductControllerBinding
import com.andruszkiewicz.internetshop.domain.model.ProductModel
import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import com.andruszkiewicz.internetshop.presentation.addProduct.AddProductActivity
import com.andruszkiewicz.internetshop.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class ProductControllerActivity : AppCompatActivity() {

    private val TAG = ProductControllerActivity::class.java.simpleName

    @Inject
    lateinit var repository: ProductRepository

    private var _binding: ActivityProductControllerBinding? = null
    private val binding get() =  _binding!!

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: ProductControllerRecycleView

    private var listOfProducts: MutableList<ProductModel> = mutableListOf()

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        takeProductFromResult(result)?.let { product ->
            lifecycleScope.launch(Dispatchers.Default) {
                updateList(product)
                withContext(Dispatchers.Main) {
                    adapter.updateList(listOfProducts)
                }
            }
        }
    }

    private fun takeProductFromResult(result: ActivityResult): ProductModel? =
        if (result.resultCode == Activity.RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra(Utils.PRODUCT_EXTRA, ProductModel::class.java)
            }
            else {
                result.data?.getParcelableExtra(Utils.PRODUCT_EXTRA)
            }
        } else null


    private fun updateList(product: ProductModel) {
        val existingProductIndex = listOfProducts.indexOfFirst { it.id == product.id }

        if (existingProductIndex != -1) {
            listOfProducts[existingProductIndex] = product
        }
        else {
            listOfProducts.add(product)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        _binding = ActivityProductControllerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initListener()
    }

    private fun initListener() {
        binding.backBnt.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.addProductFab.setOnClickListener {
            val intent = Intent(applicationContext, AddProductActivity::class.java)
            activityResultLauncher.launch(intent)
        }
    }

    private fun initView() {
        setUpAdapter()
        takeDataFromApi()
    }

    private fun setUpAdapter() {
        linearLayoutManager = LinearLayoutManager(this)
        binding.productsRv.layoutManager = linearLayoutManager

        adapter = ProductControllerRecycleView(
            products = listOfProducts,
            onClickProduct = { product ->
                val intent = Intent(this, AddProductActivity::class.java)
                intent.putExtra(Utils.PRODUCT_EXTRA, product)
                activityResultLauncher.launch(intent)
            },
            onClickDeleteProduct = { product ->
                removeProduct(product)
            }
        )

        binding.productsRv.adapter = adapter
    }

    private fun removeProduct(product: ProductModel) {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = repository.removeProduct(product.id)

            if (response != null) {
                listOfProducts.remove(response)
                withContext(Dispatchers.Main) {
                    adapter.updateList(listOfProducts)
                    Utils.toast(
                        message = "Remove product!",
                        context = applicationContext
                    )
                }
            } else {
                withContext(Dispatchers.Main) {
                    Utils.toast(
                        message = "Can`t remove product!",
                        context = applicationContext
                    )
                }
            }
        }
    }

    private fun takeDataFromApi() {
        lifecycleScope.launch(Dispatchers.IO) {
            listOfProducts = repository.getProducts().toMutableList()
            withContext(Dispatchers.Main) {
                adapter.updateList(listOfProducts)
            }
        }
    }
}