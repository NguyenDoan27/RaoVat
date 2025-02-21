package com.example.raovat_app.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.raovat_app.R
import com.example.raovat_app.classes.Cart
import com.example.raovat_app.classes.Product
import com.example.raovat_app.interfaces.ICartChecked
import com.example.raovat_app.interfaces.ICartMinus
import com.example.raovat_app.interfaces.ICartPlus
import com.example.raovat_app.interfaces.ICartRemove
import com.example.raovat_app.interfaces.ICartUnChecked
import com.example.raovat_app.others.FormatPrice

class CartAdapter(private val carts: List<Cart>,
                  private val product: List<Product>,
                  private var total: TextView,
                  private val checked: ICartChecked,
                  private val unChecked: ICartUnChecked,
                  private val plus: ICartPlus,
                  private val minus: ICartMinus,
                  private val remove: ICartRemove
): RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imgProduct_cartItem)
        private val tvName: TextView = itemView.findViewById(R.id.tvNameProduct_cartItem)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPriceProduct_cartItem)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity_cartItem)
        val imgMinus: ImageView = itemView.findViewById(R.id.imgMinus_cartItem)
        val imgPlus: ImageView = itemView.findViewById(R.id.imgPlus_cartItem)
        val cb: CheckBox = itemView.findViewById(R.id.cb_cartItem)

        @SuppressLint("SetTextI18n")
        fun bind(cart: Cart, product: List<Product>) {
            tvQuantity.text = cart.getAmount().toString()
            for ( i in product){
                if(cart.getIdProduct() == i.getIdProduct()){
                    tvName.text = i.getName()
                    tvPrice.text = FormatPrice.formatPrice(i.getPrice().toString())
                    Glide.with(itemView.context).load(i.getImage()).into(imageView)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = carts.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(carts[position], product)
        holder.itemView.setOnClickListener{
            holder.cb.isChecked = !holder.cb.isChecked
            if(holder.cb.isChecked){
                for (i in product){
                    total.text = FormatPrice.formatPrice((total.text.toString()
                        .replace("đ", "")
                        .trim().replace(",", "")
                        .toInt() + i.getPrice()!!).toString())
                }
                checked.onCheck(carts[position].getIdCart()!!)
            }else{
                for (i in product){
                    total.text = FormatPrice.formatPrice((total.text.toString()
                        .replace("đ", "")
                        .trim().replace(",", "")
                        .toInt() - i.getPrice()!!).toString())
                }
                unChecked.onUnChecked(carts[position].getIdCart()!!)
            }
        }

        holder.imgPlus.setOnClickListener {
            var quantity = holder.tvQuantity.text.toString().toInt()
            quantity++
            carts[position].setAmount(quantity)
            holder.tvQuantity.text = quantity.toString()
            plus.onPlus(carts[position])
        }

        holder.imgMinus.setOnClickListener {
            var quantity = holder.tvQuantity.text.toString().toInt()
            quantity--
            if (quantity >= 1) {
                carts[position].setAmount(quantity)
                holder.tvQuantity.text = quantity.toString()
                minus.onMinus(carts[position])
            }else{
                remove.onRemove(carts[position].getIdCart()!!)

            }
        }
    }



}