package com.andruszkiewicz.internetshop.presentation.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.andruszkiewicz.internetshop.R
import com.andruszkiewicz.internetshop.databinding.ProductViewBinding
import com.andruszkiewicz.internetshop.domain.model.ProductModel
import com.andruszkiewicz.internetshop.domain.model.UserModel
import com.andruszkiewicz.internetshop.domain.mapper.toPrize

class ProductRecyclerView(
    private var items: List<ProductModel>,
    private val onClickAddItem: (ProductModel?) -> Unit,
    private var user: UserModel? = null
): RecyclerView.Adapter<ProductRecyclerView.ProductViewHolder>() {

    private val TAG = ProductRecyclerView::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductViewBinding.inflate(inflater)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = items[position]
        val orderProducts = user?.order?.products

        Log.d(TAG, "onBindViewHolder: $orderProducts")

        with(holder.binding) {
            productTv.text = item.name
            prizeTv.text = item.prize.toPrize()
            if (item == items.last()) productDivider.visibility = View.GONE

            if (orderProducts?.find { it.product.id == item.id } != null) {
                buyIb.setImageResource(R.drawable.ic_check_mark_white)
                buyIb.setOnClickListener {
                    onClickAddItem(null)
                }
            } else {
                buyIb.setImageResource(R.drawable.ic_add_schopping_cart_white)
                buyIb.setOnClickListener {
                    onClickAddItem(item)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newListOfItems: List<ProductModel>) {
        items = newListOfItems
        notifyDataSetChanged()
    }

    fun updateUser(incomeUser: UserModel) {
        Log.d(TAG, "updateUser: $incomeUser")
        user = incomeUser
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(val binding: ProductViewBinding): ViewHolder(binding.root)
}