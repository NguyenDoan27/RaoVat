package com.example.raovat_app.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.raovat_app.R
import com.example.raovat_app.classes.Product
import com.example.raovat_app.interfaces.IAccessOnClick
import com.example.raovat_app.interfaces.ICancelOnClick

class StoreConfirmAdapter(private val products : List<Product>, private val access: IAccessOnClick, private val cancel: ICancelOnClick): RecyclerView.Adapter<StoreConfirmAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imgStoreConfirmProduct)
        val name : TextView = view.findViewById(R.id.tvStoreConfirmProductName)
        val price: TextView = view.findViewById(R.id.tvStoreConfirmProductPrice)
        val quantity: TextView = view.findViewById(R.id.tvStoreConfirmProductAmount)
        val access: Button = view.findViewById(R.id.btnStoreConfirmProductAccess)
        val cancel: Button = view.findViewById(R.id.btnStoreConfirmProductCancel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(parent.context, R.layout.store_confirm_item, null)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int =products.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        Glide.with(holder.itemView.context).load(product.getImage()).into(holder.imageView)
        holder.name.text = product.getName()
        holder.price.text = product.getPrice().toString()
        holder.quantity.text = "Số lượng: ${product.getAmount()}"
        holder.access.setOnClickListener{
            access.accessClick(product.getIdCart()!!)
        }
        holder.cancel.setOnClickListener{
            cancel.cancelClick(product.getIdCart()!!)
        }
    }

}