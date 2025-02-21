package com.example.raovat_app.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raovat_app.adapters.StoreConfirmAdapter
import com.example.raovat_app.classes.Product
import com.example.raovat_app.classes.Response
import com.example.raovat_app.controllers.StoreController
import com.example.raovat_app.databinding.FragmentStoreConfirmBinding
import com.example.raovat_app.interfaces.IAccessOnClick
import com.example.raovat_app.interfaces.ICancelOnClick
import com.example.raovat_app.interfaces.ResponseCallback
import com.example.raovat_app.models.StoreModel
import kotlinx.coroutines.launch

class StoreConfirmFragment : Fragment(), IAccessOnClick, ICancelOnClick, ResponseCallback {

    private lateinit var binding: FragmentStoreConfirmBinding
    private var id = ""
    private var storeModel: StoreModel? = null
    private var storeController: StoreController? = null
    private var adapter: StoreConfirmAdapter? = null
    private var listProduct: List<Product>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id = arguments?.getString("id").toString()
        storeModel = StoreModel()
        storeController = StoreController(storeModel!!, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStoreConfirmBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun accessClick(id: Int) {
        Toast.makeText(context, "Access $id", Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            storeController?.accessOder(id)
        }
        onResume()
    }

    override fun cancelClick(id: Int) {
       lifecycleScope.launch {
           storeController?.cancelOder(id)
       }
        onResume()
    }

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    override fun onSuccess(response: Response<*>) {
        when(response){
            is Response.Success -> {
                binding.rcvConfirm.visibility = View.VISIBLE
                binding.tvNoItem.visibility = View.GONE
                listProduct = response.value as List<Product>
                adapter = StoreConfirmAdapter(listProduct!!, this, this)
                binding.rcvConfirm.layoutManager = LinearLayoutManager(context)
                adapter?.notifyDataSetChanged()
                binding.rcvConfirm.adapter = adapter
                }
            is Response.SuccessWithExtra<*,*> -> {
                if(response.value as Boolean){
                    showMessenger("Đơn hàng đã được ${response.value1}")
                    onResume()
                }
            }
            is Response.SuccessWith3Params<*,*,*> -> {
                TODO("Not yet implemented")
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onError(error: String) {
        binding.rcvConfirm.visibility = View.GONE
        binding.tvNoItem.visibility = View.VISIBLE
        binding.tvNoItem.text = error
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showMessenger(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            storeController?.getStoreConfirm(id)
        }
    }

}