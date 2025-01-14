package com.andruszkiewicz.internetshop.presentation.orderHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andruszkiewicz.internetshop.databinding.OrderViewBinding
import com.andruszkiewicz.internetshop.domain.mapper.toPrize
import com.andruszkiewicz.internetshop.domain.model.OrderModel

class HistoryOrderRecycleView(
    private var orders: List<OrderModel>
): RecyclerView.Adapter<HistoryOrderRecycleView.HistoryOrderViewHolder>() {

    private val TAG = HistoryOrderRecycleView::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryOrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OrderViewBinding.inflate(inflater)
        return HistoryOrderViewHolder(binding)
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: HistoryOrderViewHolder, position: Int) {
        val order = orders[position]

        with(holder.binding) {
            idOrderTv.text = "Id order: #${order.id}"

            val totalPrize = order
                .products
                .sumOf { (it.product.prize * it.quantity).toDouble() }
                .toFloat()

            prizeTv.text = "Prize: ${totalPrize.toPrize()}"

            orderProductsTv.text = order.products.joinToString("\n") {
                "Product: ${it.product.name} - ${it.quantity} pieces"
            }
        }
    }

    fun updateList(newListOfOrders: List<OrderModel>) {
        orders = newListOfOrders
        notifyDataSetChanged()
    }

    inner class HistoryOrderViewHolder(val binding: OrderViewBinding): RecyclerView.ViewHolder(binding.root)
}