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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raovat_app.interfaces.IRecycleViewOnClick
import com.example.raovat_app.activities.DetailProductActivity
import com.example.raovat_app.activities.ReserveProductActivity
import com.example.raovat_app.adapters.CartAdapter
import com.example.raovat_app.adapters.HFProductAdapter
import com.example.raovat_app.classes.Cart
import com.example.raovat_app.classes.Response
import com.example.raovat_app.classes.Product
import com.example.raovat_app.controllers.CartController
import com.example.raovat_app.controllers.ProductController
import com.example.raovat_app.databinding.FragmentCartBinding
import com.example.raovat_app.models.CartModel
import com.example.raovat_app.models.ProductModels
import com.example.raovat_app.interfaces.ICartCallback
import com.example.raovat_app.interfaces.ICartChecked
import com.example.raovat_app.interfaces.ICartMinus
import com.example.raovat_app.interfaces.ICartPlus
import com.example.raovat_app.interfaces.ICartRemove
import com.example.raovat_app.interfaces.ICartUnChecked
import com.example.raovat_app.others.PreferenceDataStore
import com.example.raovat_app.interfaces.ResponseCallback
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment(),
    ICartCallback,
    ICartChecked,
    ICartUnChecked,
    ICartPlus,
    ICartMinus, ICartRemove,
    ResponseCallback {
    lateinit var binding: FragmentCartBinding
    private val ids = ArrayList<Int>()

    private var cartController : CartController? = null
    private var  productController: ProductController? = null

    companion object {
        @JvmStatic
        fun newInstance() = CartFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         val preferenceDataStore = PreferenceDataStore(requireContext())
         val cartModel = CartModel(preferenceDataStore)
         cartController = CartController(cartModel, this)

        val productModel = ProductModels()
        productController = ProductController(productModel, this)


        binding.btnOrder.setOnClickListener{
            if (ids.isNotEmpty()){
               val intent = Intent(requireContext(), ReserveProductActivity::class.java)
                intent.putIntegerArrayListExtra("ids", ids)
                startActivity(intent)

            }else{
                showMessenger("Ban chua chon san pham nao!!")
            }
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            cartController!!.getCart()
            productController!!.getAllProduct()
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onSuccess(response: Response<*>) {
        when (response){
            is Response.Success -> {
                val adapter = HFProductAdapter(response.value as List<Product>, object : IRecycleViewOnClick {
                    override fun onClick(position: Int) {
                        val intent = Intent(requireContext(), DetailProductActivity::class.java)
                        intent.putExtra("id", response.value[position].getIdProduct())
                        startActivity(intent)
                    }
                })
                binding.rcvCartProduct.adapter = adapter
            }
            is Response.SuccessWithExtra<*,*> -> {
                if (response.value != null){
                    Log.e("TAG", "onSuccess: ", )
                    val adapter = CartAdapter(response.value as List<Cart>,
                        response.value1 as List<Product>,
                        binding.tvTotal ,
                        this, this, this, this, this)
                    binding.rcvCart.layoutManager = LinearLayoutManager(requireContext())
                    binding.rcvCart.adapter = adapter
                    binding.rcvCart.visibility = View.VISIBLE
                    binding.layoutEmpty.visibility = View.GONE
                }else{
                    binding.rcvCart.visibility = View.GONE
                }
            }
            is Response.SuccessWith3Params<*,*,*> -> {
                //Toast.makeText(binding.root.context, "update so luong", Toast.LENGTH_SHORT).show()
                onResume()
            }

        }
    }

    override fun onError(error: String) {
        Log.e("TAG", "onError: ", )
        binding.tvEmpty.text = error
        binding.rcvCart.visibility = View.GONE
        binding.layoutEmpty.visibility = View.VISIBLE
    }

    private fun showMessenger( msg: String){
        Toast.makeText(binding.root.context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onUnChecked(id: Int) {
        ids.remove(id)
    }

    override fun onCheck(id: Int) {
        ids.add(id)
    }

    override fun onPlus(cart: Cart) {
        lifecycleScope.launch {
            cartController!!.updateCart(cart)
        }
    }

    override fun onMinus(cart: Cart) {
        lifecycleScope.launch {
            cartController!!.updateCart(cart)
        }
    }

    override fun onRemove(id: Int) {
        lifecycleScope.launch {
            cartController!!.removeCart(id)
        }
    }
}