package com.andruszkiewicz.internetshop.presentation.addProduct

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.andruszkiewicz.internetshop.databinding.ActivityAddProductBinding
import com.andruszkiewicz.internetshop.domain.mapper.toPrize
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

    private var name = ""
    private var prize: Float? = null
    private var product: ProductModel? = null

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
        name = binding.nameEt.text.toString().trim()
        prize = binding.prizeEt.text.toString().trim().toFloatOrNull()

        if (name.isBlank()) {
            binding.nameEt.error = "Enter name of product"
            binding.nameEt.requestFocus()
        } else if (prize == null) {
            binding.prizeEt.error = "Wrong prize"
            binding.prizeEt.requestFocus()
        } else {
            addEditProduct()
        }
    }

    private fun addEditProduct() {
        lifecycleScope.launch(Dispatchers.IO) {
            product = ProductModel(
                id = product?.id ?: 0,
                name = name,
                prize = prize!!
            )

            val result = repository.createEditProduct(product!!)

            if (result != null) {
                withContext(Dispatchers.Main) {
                    val resultIntent = intent
                    resultIntent.putExtra(Utils.PRODUCT_EXTRA, result)
                    setResult(Activity.RESULT_OK, resultIntent)
                    Utils.toast(
                        message = "Add new product!",
                        context = applicationContext
                    )
                    finish()
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
        product = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Utils.PRODUCT_EXTRA, ProductModel::class.java)
        } else {
            intent.getParcelableExtra(Utils.PRODUCT_EXTRA)
        }

        if (product != null) setUpEditView()
    }

    private fun setUpEditView() {
        with(binding) {
            titleTv.text = "Edit Product"
            addBnt.text = "Edit Product"

            nameEt.setText(product!!.name)
            nameEt.isEnabled = false

            prizeEt.setText(product!!.prize.toPrize().dropLast(3))
        }
    }
}