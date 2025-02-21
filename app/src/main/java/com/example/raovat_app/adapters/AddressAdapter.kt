package com.example.raovat_app.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.raovat_app.R
import com.example.raovat_app.classes.Address
import com.example.raovat_app.interfaces.IAddressItemClick

class AddressAdapter (private val addressList: List<Address>, private val click: IAddressItemClick) : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName = itemView.findViewById<TextView>(R.id.tvAddress_Username)
        private val tvPhone = itemView.findViewById<TextView>(R.id.tvAddress_Phone)
        private val tvKind = itemView.findViewById<TextView>(R.id.tvAddress_Kind)
        private val tvDetail = itemView.findViewById<TextView>(R.id.tvAddress_Detail)

        @SuppressLint("SetTextI18n")
        fun bind(address: Address) {
            tvName.text = address.getName()
            tvPhone.text = address.getPhone()
            tvKind.text = address.getNameKind()
            tvDetail.text = "${address.getVillage()}, ${address.getDistrict()}, ${address.getWard()}, ${address.getCity()}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.address_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = addressList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(addressList[position])

        holder.itemView.setOnClickListener {
            click.onItemClick(addressList[position])
        }
    }
}