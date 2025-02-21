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
import com.example.raovat_app.others.FormatPrice

class HFProductAdapter(private val products : List<Product>, private val click: IRecycleViewOnClick):
    RecyclerView.Adapter<HFProductAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imageView: ImageView = view.findViewById(R.id.img_product)
        val name: TextView = view.findViewById(R.id.tvName_Product)
        val price: TextView = view.findViewById(R.id.tvPrice_Product)
        val location: TextView = view.findViewById(R.id.tvLocation_Product)
        val sold: TextView = view.findViewById(R.id.tvSold_Product)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = products.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = products[position].getName()
        Glide.with(holder.itemView.context).load(products[position].getImage()).into(holder.imageView)
        holder.price.text = FormatPrice.formatPrice(products[position].getPrice().toString())
        holder.location.text = products[position].getAddress()
        holder.sold.text = "Đã bán:  ${products[position].getSales().toString()}"

        holder.itemView.setOnClickListener{
            click.onClick(position)
        }
    }


}