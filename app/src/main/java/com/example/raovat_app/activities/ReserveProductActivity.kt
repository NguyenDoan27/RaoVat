package com.example.raovat_app.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raovat_app.R
import com.example.raovat_app.adapters.ReserveAdapter
import com.example.raovat_app.classes.Address
import com.example.raovat_app.classes.Cart
import com.example.raovat_app.classes.Product
import com.example.raovat_app.classes.Response
import com.example.raovat_app.controllers.AccountController
import com.example.raovat_app.controllers.CartController
import com.example.raovat_app.controllers.ProductController
import com.example.raovat_app.databinding.ActivityReserveProductBinding
import com.example.raovat_app.interfaces.ICartCallback
import com.example.raovat_app.interfaces.ResponseCallback
import com.example.raovat_app.models.AccountModel
import com.example.raovat_app.models.CartModel
import com.example.raovat_app.models.ProductModels
import com.example.raovat_app.others.FormatPrice
import com.example.raovat_app.others.PreferenceDataStore
import kotlinx.coroutines.launch

class ReserveProductActivity : AppCompatActivity(), ResponseCallback, ICartCallback {
    private lateinit var binding: ActivityReserveProductBinding
    private var preferenceDataStore: PreferenceDataStore? = null
    private var accountModel: AccountModel? = null
    private var address: Address? = null
    private var accountController: AccountController? = null
    private var products: ArrayList<Product>? = null
    private var adapter: ReserveAdapter? = null
    private var ids = ArrayList<Int>()
    private var productController: ProductController? = null
    private var productModel: ProductModels? = null
    private var cartController: CartController? = null
    private var cartModel: CartModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReserveProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        preferenceDataStore = PreferenceDataStore(this)
        accountModel = AccountModel(preferenceDataStore!!)
        accountController = AccountController(accountModel!!, this)
        ids = intent.getIntegerArrayListExtra("ids") as ArrayList<Int>
        Log.e("TAG", "onCreate: $ids")
        productModel = ProductModels()
        productController = ProductController(productModel!!, this)

        cartModel = CartModel(preferenceDataStore!!)
        cartController = CartController(cartModel!!, this)


        binding.btnReserve.setOnClickListener{
            order(ids)
        }
    }

    override fun onResume() {
        super.onResume()

        fetchData()
    }
    private fun fetchData() {
        lifecycleScope.launch {
            accountController?.getAddressDefault()
            productController?.getOrdersById(ids)
        }
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onSuccess(response: Response<*>) {
        when(response){
            is Response.Success -> {
                products?.clear()
                products = response.value as ArrayList<Product>
                binding.tvTotal.text = FormatPrice.formatPrice(calculateTotal(products!!, 30000, 1).toString())
                binding.tvTotal1.text = FormatPrice.formatPrice(calculateTotal(products!!, 30000, 0).toString())
                binding.tvTotal2.text = FormatPrice.formatPrice(calculateTotal(products!!, 30000, 1).toString())
                binding.tvShipment.text = FormatPrice.formatPrice("30000")
                adapter = ReserveAdapter(products!!)
                binding.rcvCart.layoutManager = LinearLayoutManager(this)
                adapter!!.notifyDataSetChanged()
                binding.rcvCart.adapter = adapter
            }
            is Response.SuccessWithExtra<*, *> -> {
                address = response.value as Address
                bindAddress(address!!)
            }
            is Response.SuccessWith3Params<*, *, *> ->{
                showMessage(response.value as String)
            }
        }
    }

    override fun onError(error: String) {
        showMessage(error)
    }

    private fun showMessage(message: String){
        Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
    }
    @SuppressLint("SetTextI18n")
    private fun bindAddress(address: Address){
        if (address.getName() != null){
            binding.tvNameUser.text = address.getName()
            binding.tvPhoneUser.text = address.getPhone()
            binding.tvAddress.text = "${address.getVillage()}, ${address.getDistrict()}, ${address.getWard()}, ${address.getCity()}"
            binding.tvKind.text = address.getNameKind()
        }
    }

    private fun calculateTotal(products: ArrayList<Product>, ship: Int, type: Int ): Int{
        var total = 0
        for (i in products){
            total += i.getPrice()!! * i.getAmount()!!
        }
        when(type){
            0 -> return total
            else -> return total + ship
        }

    }

    private fun order(ids: ArrayList<Int>){
        lifecycleScope.launch {
            cartController!!.order(ids)
        }
    }

}