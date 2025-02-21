package com.example.raovat_app.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.raovat_app.adapters.HFProductAdapter
import com.example.raovat_app.classes.Product
import com.example.raovat_app.classes.Response
import com.example.raovat_app.controllers.StoreController
import com.example.raovat_app.databinding.FragmentProductStoreBinding
import com.example.raovat_app.interfaces.IProductOnClick
import com.example.raovat_app.interfaces.IRecycleViewOnClick
import com.example.raovat_app.interfaces.ResponseCallback
import com.example.raovat_app.models.StoreModel
import kotlinx.coroutines.launch


class ProductStoreFragment : Fragment() , ResponseCallback, IRecycleViewOnClick{
    private lateinit var binding: FragmentProductStoreBinding
    private var storeModel: StoreModel? = null
    private var storeController: StoreController? = null
    private var id: String? = null
    private var adapter: HFProductAdapter? = null
    private var listener: IProductOnClick? = null
    private var products: List<Product>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id = arguments?.getString("id").toString()
        storeModel = StoreModel()
        storeController = StoreController(storeModel!!, this)
        showMessenger("id: $id")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? IProductOnClick
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductStoreBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            storeController!!.getProduct(id!!.toInt())
        }
    }
    @Suppress("UNCHECKED_CAST")
    override fun onSuccess(response: Response<*>) {
        when(response){
            is Response.Success -> {
                products = emptyList()
                products = response.value as List<Product>
                binding.rcvStore.visibility = View.VISIBLE
                adapter = HFProductAdapter(products!!, this)
                binding.rcvStore.adapter = adapter
            }
            is Response.SuccessWithExtra<*,*> -> {

            }
            is Response.SuccessWith3Params<*,*,*> -> {

            }
        }
    }

    override fun onError(error: String) {
        showMessenger(error)
        binding.rcvStore.visibility = View.GONE
    }

    private fun showMessenger(msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("CommitTransaction")
    override fun onClick(position: Int) {
        Log.e("TAG", "onClick: ${products!![position].getIdProduct()!!}", )
        listener!!.onProductClick(products!![position].getIdProduct()!!)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

}