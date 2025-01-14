package com.andruszkiewicz.internetshop.presentation.userController

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.andruszkiewicz.internetshop.databinding.UserControllerViewBinding
import com.andruszkiewicz.internetshop.domain.model.UserEmailAndStatusModel
import com.andruszkiewicz.internetshop.domain.model.UserModel

class UserControllerRecycleView(
    private var listOfUsers: MutableList<UserEmailAndStatusModel> = mutableListOf(),
    private val onClickUser: (UserEmailAndStatusModel) -> Unit
): RecyclerView.Adapter<UserControllerRecycleView.UserControllerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserControllerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = UserControllerViewBinding.inflate(inflater)
        return UserControllerViewHolder(binding)
    }

    override fun getItemCount(): Int = listOfUsers.size

    override fun onBindViewHolder(holder: UserControllerViewHolder, position: Int) {
        val user = listOfUsers[position]

        with(holder.binding) {
            emailTv.text = "${user.email}"
            isAdminTv.text = "Status: ${user.status}"

            userControllerRl.setOnClickListener {
                onClickUser(user)
            }
        }

        if (user == listOfUsers.last())
            holder.binding.divider.visibility = View.GONE
    }

    fun updateList(newListOfUsers: MutableList<UserEmailAndStatusModel>) {
        listOfUsers = newListOfUsers
        notifyDataSetChanged()
    }

    inner class UserControllerViewHolder(val binding: UserControllerViewBinding): ViewHolder(binding.root)

}