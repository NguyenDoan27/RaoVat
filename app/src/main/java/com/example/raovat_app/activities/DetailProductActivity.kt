package com.example.raovat_app.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.raovat_app.interfaces.IRecycleViewOnClick
import com.example.raovat_app.R
import com.example.raovat_app.adapters.ProductHorizontalAdapter
import com.example.raovat_app.classes.Cart
import com.example.raovat_app.classes.Response
import com.example.raovat_app.classes.Image
import com.example.raovat_app.classes.Product
import com.example.raovat_app.classes.Store
import com.example.raovat_app.controllers.CartController
import com.example.raovat_app.controllers.ProductController
import com.example.raovat_app.databinding.ActivityDetailProductBinding
import com.example.raovat_app.models.CartModel
import com.example.raovat_app.models.ProductModels
import com.example.raovat_app.others.FormatPrice
import com.example.raovat_app.interfaces.ICartCallback
import com.example.raovat_app.others.PreferenceDataStore
import com.example.raovat_app.interfaces.ResponseCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class DetailProductActivity : AppCompatActivity(), ICartCallback,
    ResponseCallback {

    lateinit var binding: ActivityDetailProductBinding
    private val productModel = ProductModels()
    private val productController = ProductController(productModel, this)
    private var product: Product? = null
    private var image: String? = null
    private val preferenceDataStore = PreferenceDataStore(this@DetailProductActivity)
    private val cartController = CartController(CartModel(preferenceDataStore), this)
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarDetailProduct.setNavigationIcon(R.drawable.back)
        binding.toolbarDetailProduct.setNavigationOnClickListener {
            finish()
        }

        val id = intent.extras?.getInt("id")
        this.id = id

        lifecycleScope.launch {
            productController.getProductById(id!!)
        }

        binding.layoutMoreDetail.setOnClickListener{
            showDialogDetail(product!!)
        }

        binding.btnBuyNow.setOnClickListener{
            showDialogSelectOption(product!!, R.id.btnBuy)
        }

        binding.btnAddToCart.setOnClickListener{
            showDialogSelectOption(product!!, R.id.btnAddCart)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fillData(product: Product, store: Store, images: List<Image>){
        this.product = product
        val img = ArrayList<SlideModel>()
        binding.tvNameProduct.text = product.getName()
        binding.tvPrice.text = FormatPrice.formatPrice(product.getPrice().toString())
        binding.tvNameStore.text = store.getName()
        binding.tvLocationStore.text = store.getAddress()
        binding.tvDescription.text = product.getDescription()
        binding.tvSales.text = "Đá bán: ${product.getSales()}"
        images.map { img.add(SlideModel(it.getUrl(), ScaleTypes.CENTER_INSIDE)) }
        this.image = images[0].getUrl()
        binding.imgSlider.setImageList(img)
    }

    private fun fillListProduct(list: List<Product>){
        Snackbar.make(binding.root, "thành công", Snackbar.LENGTH_SHORT).show()
        val adapter = ProductHorizontalAdapter(list, object : IRecycleViewOnClick {
            override fun onClick(position: Int) {
                lifecycleScope.launch {
                    list[position].getIdProduct()
                        ?.let { productController.getProductById(it) }
                }
            }
        })
        binding.rvProduct.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
        binding.rvProduct.adapter = adapter
    }

    @SuppressLint("MissingInflatedId", "InflateParams")
    fun showDialogDetail(product: Product){
        val dialog = BottomSheetDialog(this@DetailProductActivity)
        val view = layoutInflater.inflate(R.layout.more_prodcut_detail_dialog, null)
        dialog.setContentView(view)

        view.findViewById<ImageView>(R.id.imgQuit).setOnClickListener{
            dialog.dismiss()
        }

        fillDataDetail(product, view)
        dialog.show()
    }

    private fun fillDataDetail(product: Product, view: View){
        view.findViewById<TextView>(R.id.tvMaterial).text = product.getMaterial()
        view.findViewById<TextView>(R.id.tvBrand).text = product.getTrademark()
        view.findViewById<TextView>(R.id.tvOrigin).text = product.getOrigin()
    }

    @SuppressLint("InflateParams", "MissingInflatedId", "SetTextI18n")
    fun  showDialogSelectOption(product: Product, id: Int){
        val dialog = BottomSheetDialog(this@DetailProductActivity)
        val view = layoutInflater.inflate(R.layout.select_option_dialog, null)
        dialog.setContentView(view)

        view.findViewById<ImageView>(R.id.imgQuit).setOnClickListener{
            dialog.dismiss()
        }

        view.findViewById<ImageView>(R.id.imgMinus).setOnClickListener{
            val quantity = view.findViewById<TextView>(R.id.tvQuantity)
            if (quantity.text.toString().toInt() == 1){
                return@setOnClickListener
            }
            quantity.text = (quantity.text.toString().toInt() - 1).toString()
        }

        view.findViewById<ImageView>(R.id.imgPlus).setOnClickListener{
            val quantity = view.findViewById<TextView>(R.id.tvQuantity)
            quantity.text = (quantity.text.toString().toInt() + 1).toString()
        }

        when(id){
            R.id.btnBuy -> {
                val btn = view.findViewById<TextView>(R.id.btnAddCart)
                btn.visibility = View.GONE
            }
            R.id.btnAddCart -> {
                val btn = view.findViewById<TextView>(R.id.btnBuy)
                btn.visibility = View.GONE
            }
        }

        fillDataSelectOption(product, view)

        view.findViewById<Button>(R.id.btnBuy).setOnClickListener{
            val intent = Intent(this@DetailProductActivity, ReserveProductActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.btnAddCart).setOnClickListener{
            lifecycleScope.launch {
                cartController.addToCart(product.getIdProduct()!!,
                    Cart(view.findViewById<TextView>(R.id.tvQuantity).text.toString().toInt()))
            }

        }

        dialog.show()
    }

    private fun fillDataSelectOption(product: Product, view: View){
        Glide.with(view.context).load(image).into(view.findViewById(R.id.imgProduct))
        view.findViewById<TextView>(R.id.tvNameProduct).text = product.getName()
        view.findViewById<TextView>(R.id.tvPriceProduct).text = FormatPrice.formatPrice(product.getPrice().toString())
    }

    private fun showMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onSuccess(response: Response<*>) {
        when(response){
            is Response.Success -> {
                showMessage(response.value.toString())
            }
            is Response.SuccessWithExtra<*,*> -> {
                fillListProduct(response.value as List<Product>)
            }

            is Response.SuccessWith3Params<*,*,*> -> {
                fillData(response.value as Product, response.value1 as Store, response.value2 as List<Image>)
            }
        }
    }

    override fun onError(error: String) {
        showMessage(error)
    }

}