package com.example.raovat_app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.raovat_app.R
import com.example.raovat_app.adapters.StoreViewPage2Adapter
import com.example.raovat_app.classes.Response
import com.example.raovat_app.classes.Store
import com.example.raovat_app.controllers.StoreController
import com.example.raovat_app.databinding.ActivityStoreBinding
import com.example.raovat_app.fragments.AddNewFragment
import com.example.raovat_app.interfaces.IProductOnClick
import com.example.raovat_app.interfaces.IRecycleViewOnClick
import com.example.raovat_app.models.StoreModel
import com.example.raovat_app.others.MaskPhoneNumber
import com.example.raovat_app.interfaces.ResponseCallback
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch


class StoreActivity : AppCompatActivity(), ResponseCallback, IProductOnClick {
    private lateinit var binding : ActivityStoreBinding
    private val storeModel = StoreModel()
    private val storeController = StoreController(storeModel, this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idStore = intent.getStringExtra("id")

        val adapter = idStore?.let { StoreViewPage2Adapter(supportFragmentManager, lifecycle, it) }
        binding.vp2ContentStore.adapter = adapter

        TabLayoutMediator(binding.tlStore, binding.vp2ContentStore){
            tab,pos -> when (pos){
                0 -> {tab.text = "Sản phẩm"}
                1 -> {tab.text = "Doanh thu"}
                2 -> {tab.text = "Xác nhận đơn hàng"}
                else -> {tab.text = "Thêm mới"}
            }
        }.attach()

        if(idStore != null){
            lifecycleScope.launch {
                storeController.getStoreInfo(idStore)
            }
        }

    }

    private fun showMessenger(messenger: String) {
        Snackbar.make(binding.root, messenger, Snackbar.LENGTH_SHORT).show()
    }

    private fun showStoreInfo(store: Store) {
        binding.tvNameStore.text = store.getName()
        binding.tvPhoneStore.text = store.getPhone()?.let { MaskPhoneNumber.mask(it) }
        binding.tvLocationStore.text = store.getAddress()

    }

    override fun onSuccess(response: Response<*>) {
        when(response){
            is Response.Success -> TODO()
            is Response.SuccessWithExtra<*,*> -> {
                showStoreInfo(response.value as Store)
            }
            is Response.SuccessWith3Params<*,*,*> -> TODO()
        }
    }

    override fun onError(error: String) {
        showMessenger(error)
    }

    override fun onProductClick(key: Int) {

        Log.e("TAG", "onProductClick: $key", )
        (binding.vp2ContentStore.adapter as StoreViewPage2Adapter).setProductKey(key)
        binding.vp2ContentStore.apply {
            currentItem = 3
        }
    }


}