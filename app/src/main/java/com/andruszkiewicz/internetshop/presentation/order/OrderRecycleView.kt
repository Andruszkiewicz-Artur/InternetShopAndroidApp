package com.andruszkiewicz.internetshop.presentation.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.andruszkiewicz.internetshop.databinding.ListOfProductsInCartBinding
import com.andruszkiewicz.internetshop.domain.model.QuantityModel
import com.andruszkiewicz.internetshop.domain.mapper.toPrize

class OrderRecycleView(
    private var products: List<QuantityModel>,
    private val onClickChangeStateOfQuantity: (TypeOfButtonClickInOrder, QuantityModel) -> Unit
): RecyclerView.Adapter<OrderRecycleView.ProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListOfProductsInCartBinding.inflate(inflater)
        return ProductViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = products[position]

        with(holder.binding) {
            productNameTv.text = item.product.name
            prizeTv.text = (item.product.prize * item.quantity.toFloat()).toPrize()
            countTv.text = item.quantity.toString()

            removeBnt.setOnClickListener {
                onClickChangeStateOfQuantity(TypeOfButtonClickInOrder.Remove, item)
            }

            addBnt.setOnClickListener {
                onClickChangeStateOfQuantity(TypeOfButtonClickInOrder.Add, item)
            }
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateList(newList: List<QuantityModel>) {
        products = newList
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(val binding: ListOfProductsInCartBinding): ViewHolder(binding.root)

}