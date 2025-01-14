package com.andruszkiewicz.internetshop.presentation.userController

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.andruszkiewicz.internetshop.R
import com.andruszkiewicz.internetshop.databinding.ActivityUserControllerBinding
import com.andruszkiewicz.internetshop.databinding.UserControllerViewBinding
import com.andruszkiewicz.internetshop.domain.model.ProductModel
import com.andruszkiewicz.internetshop.domain.model.UserEmailAndStatusModel
import com.andruszkiewicz.internetshop.domain.model.UserModel
import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import com.andruszkiewicz.internetshop.presentation.addUser.AddUserActivity
import com.andruszkiewicz.internetshop.utils.GlobalUser
import com.andruszkiewicz.internetshop.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class UserControllerActivity : AppCompatActivity() {

    private val TAG = UserControllerActivity::class.java.simpleName

    @Inject
    lateinit var repository: ProductRepository

    private var _binding: ActivityUserControllerBinding? = null
    private val binding get() = _binding!!

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: UserControllerRecycleView

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d(TAG, "activityResultLauncher: result: $result")
        takeProductFromResult(result)?.let { user ->
            Log.d(TAG, "activityResultLauncher: user: $user")
            lifecycleScope.launch(Dispatchers.Default) {
                updateList(user)
                withContext(Dispatchers.Main) {
                    adapter.updateList(listOfUsers)
                    updateNonUserTv()
                }
            }
        }
    }

    private var listOfUsers: MutableList<UserEmailAndStatusModel> = mutableListOf()

    private fun takeProductFromResult(result: ActivityResult): UserEmailAndStatusModel? =
        if (result.resultCode == Activity.RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra(Utils.USER_EXTRA, UserEmailAndStatusModel::class.java)
            }
            else {
                result.data?.getParcelableExtra(Utils.USER_EXTRA)
            }
        } else null

    private fun updateList(user: UserEmailAndStatusModel) {
        Log.d(TAG, "updateList: listOfUsers: before: $listOfUsers")
        val existingProductIndex = listOfUsers.indexOfFirst { it.email == user.email }

        if (existingProductIndex != -1) {
            listOfUsers[existingProductIndex] = user
        }
        else {
            listOfUsers.add(user)
        }
        Log.d(TAG, "updateList: listOfUsers: after: $listOfUsers")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        _binding = ActivityUserControllerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
        initView()
        takeDataFromApi()
    }

    private fun takeDataFromApi() {
        lifecycleScope.launch(Dispatchers.IO) {
            GlobalUser.user.first()?.let { user ->
                val users = repository
                    .getUsers()
                    .toMutableList()

                users.removeIf { it.email == user.email }
                listOfUsers = users

                withContext(Dispatchers.Main) {
                    adapter.updateList(listOfUsers)
                    updateNonUserTv()
                }
            }
        }
    }

    private fun initView() {
        initAdapter()
    }

    private fun initAdapter() {
        linearLayoutManager = LinearLayoutManager(this)
        binding.usersRv.layoutManager = linearLayoutManager

        adapter = UserControllerRecycleView(
            listOfUsers = listOfUsers,
            onClickUser = { user ->
                val intent = Intent(this, AddUserActivity::class.java)
                intent.putExtra(Utils.USER_EXTRA, user)
                activityResultLauncher.launch(intent)
            }
        )

        binding.usersRv.adapter = adapter
    }

    private fun initListener() {
        binding.backBnt.setOnClickListener {
            finish()
        }

        binding.addUserFab.setOnClickListener {
            val intent = Intent(applicationContext, AddUserActivity::class.java)
            activityResultLauncher.launch(intent)
        }
    }

    private fun updateNonUserTv() {
        if (listOfUsers.isEmpty()) binding.nonUsersTv.visibility = View.VISIBLE
        else binding.nonUsersTv.visibility = View.GONE
    }
}