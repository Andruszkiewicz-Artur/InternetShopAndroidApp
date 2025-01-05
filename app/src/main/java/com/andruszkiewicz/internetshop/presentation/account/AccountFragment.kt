package com.andruszkiewicz.internetshop.presentation.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.andruszkiewicz.internetshop.databinding.FragmentAccountBinding
import com.andruszkiewicz.internetshop.domain.model.UserModel
import com.andruszkiewicz.internetshop.presentation.MainActivity
import com.andruszkiewicz.internetshop.presentation.addUser.AddUserActivity
import com.andruszkiewicz.internetshop.utils.GlobalUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private val TAG = AccountFragment::class.java.simpleName
    private val vm: AccountViewModel by viewModels()

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ArrayAdapter<String>

    private var usersList = mutableListOf<UserModel>()
    private var emailUsersList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListener()
    }

    override fun onResume() {
        super.onResume()

        vm.getUsers()
    }

    private fun initListener() {
        binding.userLv.setOnItemClickListener { parent, view, position, id ->
            val user = usersList[position]

            GlobalUser.updateUser(user)

            binding.emailTv.text = user.email
        }

        binding.addUserFab.setOnClickListener {
            val intent = Intent(requireContext(), AddUserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initView() {
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, emailUsersList)
        binding.userLv.adapter = adapter

        val email = GlobalUser.user.value?.email
        if (email != null) {
            binding.emailTv.text = email
        }

        setUpVMObserver()
    }

    private fun setUpVMObserver() {
        vm.getUsers()

        lifecycleScope.launch(Dispatchers.IO) {
            vm.users.collectLatest { users ->
                Log.d(TAG, users.toString())
                withContext(Dispatchers.Main) {
                    usersList.clear()
                    usersList.addAll(users)

                    emailUsersList.clear()
                    emailUsersList.addAll(users.map { it.email })

                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}