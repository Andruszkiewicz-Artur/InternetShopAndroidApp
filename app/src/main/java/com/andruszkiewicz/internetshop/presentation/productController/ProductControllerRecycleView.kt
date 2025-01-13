package com.andruszkiewicz.internetshop.presentation.productController

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.andruszkiewicz.internetshop.databinding.ListOfProductsInControllerBinding
import com.andruszkiewicz.internetshop.domain.mapper.toPrize
import com.andruszkiewicz.internetshop.domain.model.ProductModel

class ProductControllerRecycleView(
    private var products: List<ProductModel>,
    private val onClickProduct: (ProductModel) -> Unit,
    private val onClickDeleteProduct: (ProductModel) -> Unit
): RecyclerView.Adapter<ProductControllerRecycleView.ProductControllerViewHolder>() {

    private val TAG = ProductControllerRecycleView::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductControllerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListOfProductsInControllerBinding.inflate(inflater)
        return ProductControllerViewHolder(binding)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductControllerViewHolder, position: Int) {
        val product = products[position]

        with(holder.binding) {
            productNameTv.text = product.name
            prizeTv.text = product.prize.toPrize()

            if (position == products.size - 1)
                divider.visibility = View.GONE

            productControllerRl.setOnClickListener {
                onClickProduct(product)
            }

            removeBnt.setOnClickListener {
                onClickDeleteProduct(product)
            }
        }
    }

    fun updateList(newListOfProducts: List<ProductModel>) {
        products = newListOfProducts
        notifyDataSetChanged()
    }

    inner class ProductControllerViewHolder(val binding: ListOfProductsInControllerBinding): ViewHolder(binding.root)

}