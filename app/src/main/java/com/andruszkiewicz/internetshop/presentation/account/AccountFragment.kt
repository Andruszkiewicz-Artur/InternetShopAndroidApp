package com.andruszkiewicz.internetshop.presentation.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.andruszkiewicz.internetshop.R
import com.andruszkiewicz.internetshop.data.datastore.PreferencesDataStoreHelper
import com.andruszkiewicz.internetshop.data.datastore.PreferencesKey
import com.andruszkiewicz.internetshop.databinding.FragmentAccountBinding
import com.andruszkiewicz.internetshop.domain.enums.UserStatus
import com.andruszkiewicz.internetshop.domain.model.UserModel
import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import com.andruszkiewicz.internetshop.presentation.changePassword.ChangePasswordActivity
import com.andruszkiewicz.internetshop.presentation.login.LoginActivity
import com.andruszkiewicz.internetshop.presentation.orderHistory.OrderHistoryActivity
import com.andruszkiewicz.internetshop.presentation.productController.ProductControllerActivity
import com.andruszkiewicz.internetshop.presentation.userController.UserControllerActivity
import com.andruszkiewicz.internetshop.utils.GlobalUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private val TAG = AccountFragment::class.java.simpleName

    @Inject
    lateinit var networkRepository: ProductRepository

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

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

    private fun initListener() {
        binding.productControllerBnt.setOnClickListener {
            val intent = Intent(requireContext(), ProductControllerActivity::class.java)
            startActivity(intent)
        }

        binding.userControllerBnt.setOnClickListener {
            val intent = Intent(requireContext(), UserControllerActivity::class.java)
            startActivity(intent)
        }

        binding.ordersHistoryBnt.setOnClickListener {
            val intent = Intent(requireContext(), OrderHistoryActivity::class.java)
            startActivity(intent)
        }

        binding.changePasswordBnt.setOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.logOutBnt.setOnClickListener {
            GlobalUser.updateUser(null)

            lifecycleScope.launch(Dispatchers.IO) {
                PreferencesDataStoreHelper.removeDataFromDataStore(
                    keys = listOf(
                        PreferencesKey.EMAIL,
                        PreferencesKey.PASSWORD
                    ),
                    requireContext()
                )
            }

            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initView() {
        val currentUser = GlobalUser.user.value!!

        if (currentUser.status != UserStatus.Admin) {
            binding.productControllerBnt.visibility = View.GONE
            binding.userControllerBnt.visibility = View.GONE
        }

        binding.emailTv.text = currentUser.email
    }
}

