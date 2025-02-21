package com.example.raovat_app.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.raovat_app.interfaces.IRecycleViewOnClick
import com.example.raovat_app.activities.DetailProductActivity
import com.example.raovat_app.adapters.HFProductAdapter
import com.example.raovat_app.classes.Product
import com.example.raovat_app.classes.Response
import com.example.raovat_app.controllers.ProductController
import com.example.raovat_app.databinding.FragmentHomeBinding
import com.example.raovat_app.models.ProductModels
import com.example.raovat_app.interfaces.ResponseCallback
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), ResponseCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val productModel = ProductModels()
    private val controller = ProductController(productModel, this)

    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch{
            controller.getAllProduct()
        }

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch{
            controller.getAllProduct()
        }
    }

     private fun fillData(list: List<Product>){
        val adapter = HFProductAdapter(list, object : IRecycleViewOnClick {
            override fun onClick(position: Int) {
                val intent = Intent(requireContext(), DetailProductActivity::class.java)
                intent.putExtra("id", list[position].getIdProduct())
                startActivity(intent)
            }
        })
//        binding.rvProduct.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProduct.adapter = adapter

    }

    private fun showMessenger(message: String){
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

//    override fun onSuccess(response: List<Product>) {
//        fillData(response)
//    }

    @Suppress("UNCHECKED_CAST")
    override fun onSuccess(response: Response<*>) {
        when(response){
            is Response.Success -> {
                fillData(response.value as List<Product>)
            }

            is Response.SuccessWithExtra<*,*> -> TODO()
            is Response.SuccessWith3Params<*,*,*> -> TODO()
        }
    }

    override fun onError(error: String) {
        showMessenger(error)
    }
}