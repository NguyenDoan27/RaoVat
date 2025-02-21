package com.example.raovat_app.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.raovat_app.R
import com.example.raovat_app.activities.AccountActivity
import com.example.raovat_app.activities.AddStoreActivity
import com.example.raovat_app.activities.LoginActivity
import com.example.raovat_app.activities.StoreActivity
import com.example.raovat_app.classes.Response
import com.example.raovat_app.classes.User
import com.example.raovat_app.controllers.AccountController
import com.example.raovat_app.controllers.StoreController
import com.example.raovat_app.databinding.FragmentAccountBinding
import com.example.raovat_app.models.AccountModel
import com.example.raovat_app.models.StoreModel
import com.example.raovat_app.others.MaskPhoneNumber
import com.example.raovat_app.others.PreferenceDataStore
import com.example.raovat_app.interfaces.ResponseCallback
import kotlinx.coroutines.launch



class AccountFragment : Fragment(), ResponseCallback {
private lateinit var binding: FragmentAccountBinding
private  var accountController: AccountController? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferencesDataStore = PreferenceDataStore(requireContext())
        val accountModel = AccountModel(preferencesDataStore)
         accountController = AccountController(accountModel, this)

        val storeModel = StoreModel()
        val storeController = StoreController(storeModel, this)
        binding.layoutLoginNavigation.setOnClickListener {
            val intent = Intent(requireContext(),LoginActivity::class.java)
            startActivity(intent)
        }
        binding.layoutStoreNavigation.setOnClickListener {
            lifecycleScope.launch {
                storeController.checkStore(this@AccountFragment , preferencesDataStore)
            }
        }
        binding.layoutDetailAccount.setOnClickListener {
            val intent = Intent(requireContext(), AccountActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            accountController!!.getUser()
        }
    }

    private fun showInfo(user: User) {
        Log.e("TAG", "showInfo: ${user.getAvatar()}", )
        if (user.getAvatar() != "null"){
            Glide.with(binding.root.context).load(user.getAvatar()).into(binding.imgAvt)
        }else{
            binding.imgAvt.setImageResource(R.drawable.avatar)
        }
        binding.tvName.text = user.getName()
        binding.tvPhone.text = MaskPhoneNumber.mask(user.getPhone())
    }

    fun showMessenger(s: String) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }

    fun startStoreActivity(id: String) {
        val intent = Intent(requireContext(), StoreActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    fun startCreateStoreActivity() {
        val intent = Intent(requireContext(), AddStoreActivity::class.java)
        startActivity(intent)
    }


    override fun onSuccess(response: Response<*>) {
        when (response){
            is Response.Success -> {
                showInfo(response.value as User)
            }
            is Response.SuccessWithExtra<*,*> -> TODO()
            is Response.SuccessWith3Params<*,*,*> -> TODO()
        }
    }

    override fun onError(error: String) {
        showMessenger(error)
    }


}