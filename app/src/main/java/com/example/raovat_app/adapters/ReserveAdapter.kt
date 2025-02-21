package com.example.raovat_app.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.raovat_app.R
import com.example.raovat_app.classes.Cart
import com.example.raovat_app.classes.Product
import com.example.raovat_app.others.FormatPrice

class ReserveAdapter(private val products: ArrayList<Product>) : RecyclerView.Adapter<ReserveAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvNameShop : TextView? = null
        private var tvNameProduct : TextView? = null
        private var tvPrice : TextView? = null
        private var tvQuantity : TextView? = null
        private var imageView: ImageView? = null
        init {
            tvNameShop = itemView.findViewById(R.id.tvReserveNameShop)
            tvNameProduct = itemView.findViewById(R.id.tvReserveNameProduct)
            tvPrice = itemView.findViewById(R.id.tvReservePrice)
            tvQuantity = itemView.findViewById(R.id.tvReserveQuantity)
            imageView = itemView.findViewById(R.id.imgReserveProduct)
        }

        @SuppressLint("SetTextI18n")
        fun bind(product: Product){
            tvNameShop?.text = product.getNameStore().toString()
            tvNameProduct?.text = product.getName().toString()
            tvPrice?.text = FormatPrice.formatPrice(product.getPrice().toString())
            tvQuantity?.text = "so luong : ${product.getAmount().toString()}"
            Glide.with(itemView.context).load(product.getImage().toString()).into(imageView!!)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.reserve_item_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size
}