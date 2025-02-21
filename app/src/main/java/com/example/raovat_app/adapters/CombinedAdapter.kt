package com.example.raovat_app.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.raovat_app.R
import com.example.raovat_app.classes.Combination

class CombinedAdapter(private val dataList: List<Combination>) : RecyclerView.Adapter<CombinedAdapter.ViewHolder>()  {

    class ViewHolder(itemView: View,val adapter: CombinedAdapter): RecyclerView.ViewHolder(itemView){
        private val combined1: TextView = itemView.findViewById(R.id.tvCombined1)
        private val combined2: TextView = itemView.findViewById(R.id.tvCombined2)
        private val edPrice: EditText = itemView.findViewById(R.id.edCombinedPrice)
        private val edAmount: EditText = itemView.findViewById(R.id.edCombinedAmount)

        fun bind(item: Combination){
            combined1.text = item.getCombination1()
            combined2.text = item.getCombination2()
            edPrice.setText(item.getPrice().toString())

            edPrice.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val value = p0?.toString()?.toIntOrNull() ?: 0
                        adapter.updatePrice(position, value)
                    }
                }
            })

            edAmount.setText(item.getAmount().toString())

            edAmount.addTextChangedListener { object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val amount = p0?.toString()?.toIntOrNull() ?: 0
                        adapter.updateAmount(position, amount)

                    }
                }
            } }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.combined_item_layout, parent, false)
        return ViewHolder(itemView, this)
    }

    override fun getItemCount(): Int {
       return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
    }

    fun updatePrice(position: Int, price: Int){
        val item = dataList[position]
        item.setPrice(price)
    }

    fun updateAmount(position: Int, amount: Int){
        val item = dataList[position]
        item.setAmount(amount)
    }


}