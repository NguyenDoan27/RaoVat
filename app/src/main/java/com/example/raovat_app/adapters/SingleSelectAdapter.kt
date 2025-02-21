package com.example.raovat_app.adapters

import android.annotation.SuppressLint
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

class SingleSelectAdapter(private var list: List<Combination>) : RecyclerView.Adapter<SingleSelectAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, val adapter: SingleSelectAdapter) : RecyclerView.ViewHolder(itemView) {
        private val tvSingle: TextView = itemView.findViewById(R.id.tvSingle)
        private val edAmount: EditText = itemView.findViewById(R.id.edSingleAmount)
        private val edPrice : EditText = itemView.findViewById(R.id.edSinglePrice)

        @SuppressLint("SetTextI18n")
        fun bind(item: Combination) {
            tvSingle.text = item.getCombination1()
            edAmount.setText(item.getAmount().toString())
            edPrice.setText(item.getPrice().toString())

            edPrice.addTextChangedListener(object : TextWatcher {
                @SuppressLint("SuspiciousIndentation")
                override fun afterTextChanged(s: Editable?) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val newValue = s?.toString()?.toIntOrNull() ?: 0
                            adapter.updatePrice(position, newValue)
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })

            edAmount.addTextChangedListener { object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val newValue = p0?.toString()?.toIntOrNull() ?: 0
                        adapter.updateAmount(position, newValue)
                    }
                }
            } }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.single_item_layout, parent, false)
        return ViewHolder(itemView, this)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

     fun updatePrice(position: Int, newPrice: Int) {
        val item = list[position]
         item.setPrice(newPrice)
    }

    fun updateAmount(position: Int, newAmount: Int){
        val item = list[position]
        item.setAmount(newAmount)
    }
}