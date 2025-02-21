package com.example.raovat_app.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.raovat_app.interfaces.IRecycleViewOnClick
import com.example.raovat_app.R
import com.example.raovat_app.classes.Product
import com.example.raovat_app.others.ConvertSpToPx
import com.example.raovat_app.others.FormatPrice

class ProductHorizontalAdapter(private val products: List<Product>, private val click: IRecycleViewOnClick) : RecyclerView.Adapter<ProductHorizontalAdapter.ViewHolder>() {


    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val imageView: ImageView = view.findViewById(R.id.img_product1)
        private val name: TextView = view.findViewById(R.id.tvName_Product1)
        private val price: TextView = view.findViewById(R.id.tvPrice_Product1)
        private val location: TextView = view.findViewById(R.id.tvLocation_Product1)
        private val sold: TextView = view.findViewById(R.id.tvSold_Product1)

        @SuppressLint("SetTextI18n")
        fun bind(product: Product){
            name.text = product.getName()
            imageView.layoutParams.width = ConvertSpToPx.convertSpToPx(itemView.context, 150f).toInt()
            imageView.layoutParams.height = ConvertSpToPx.convertSpToPx(itemView.context, 130f).toInt()
            Glide.with(itemView.context)
                .load(product.getImage())
                .override(ConvertSpToPx.convertSpToPx(itemView.context, 150f).toInt(), ConvertSpToPx.convertSpToPx(itemView.context, 130f).toInt())
                .into(imageView)
            price.text = FormatPrice.formatPrice(product.getPrice().toString())
            location.text = product.getAddress()
            sold.text = "Đã bán:  ${product.getSales().toString()}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item_horizontal, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int =products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])

        holder.itemView.setOnClickListener{
            click.onClick(position)
        }
    }

}