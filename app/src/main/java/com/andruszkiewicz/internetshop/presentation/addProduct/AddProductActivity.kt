package com.andruszkiewicz.internetshop.presentation.addProduct

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.andruszkiewicz.internetshop.databinding.ActivityAddProductBinding
import com.andruszkiewicz.internetshop.domain.model.ProductModel
import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import com.andruszkiewicz.internetshop.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class AddProductActivity : AppCompatActivity() {

    @Inject
    lateinit var repository: ProductRepository

    private val TAG = AddProductActivity::class.java.simpleName

    private var _binding: ActivityAddProductBinding? = null
    private val binding get() = _binding!!

    private var productId: Long? = null
    private var productName = ""
    private var productPrize: Float? = null
    private var products: List<ProductModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initListener()
    }

    private fun initListener() {
        binding.backBnt.setOnClickListener {
            onBackPressed()
        }

        binding.addBnt.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        productName = binding.nameEt.text.toString().trim()
        productPrize = binding.prizeEt.text.toString().trim().toFloatOrNull()

        if (productName.isBlank()) {
            binding.nameEt.error = "Enter name of product"
            binding.nameEt.requestFocus()
        } else if (productPrize == null) {
            binding.prizeEt.error = "Wrong prize"
            binding.prizeEt.requestFocus()
        } else if (products.find { it.name == productName } != null) {
            binding.nameEt.error = "Product like that already exist"
            binding.nameEt.requestFocus()
        } else {
            addProduct()
        }
    }

    private fun addProduct() {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = repository.createProduct(productName, productPrize!!)

            if (result) {
                withContext(Dispatchers.Main) {
                    onBackPressed()
                    Utils.toast(
                        message = "Add new product!",
                        context = applicationContext
                    )
                }
            } else {
                Utils.toast(
                    message = "Problem with adding product!",
                    context = applicationContext
                )
            }
        }
    }

    private fun initView() {
        val idProduct = intent.getLongExtra(Utils.PRODUCT_ID_EXTRA, -1)
        if (idProduct >= 0) productId = idProduct

        getProducts()
    }

    private fun getProducts() {
        lifecycleScope.launch(Dispatchers.IO) {
            products = repository.getProducts()

            if (productId != null) setUpView()
        }
    }

    private fun setUpView() {
        binding.titleTv.text = "Edit Product"
        binding.nameEt.isEnabled = false
        binding.addBnt.text = "Edit Product"
    }
}