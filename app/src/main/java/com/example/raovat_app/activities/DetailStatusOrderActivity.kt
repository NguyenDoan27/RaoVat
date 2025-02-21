package com.example.raovat_app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.raovat_app.adapters.DetailStatusViewPage2Adapter
import com.example.raovat_app.databinding.ActivityDetailStatusOrderBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailStatusOrderActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailStatusOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStatusOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPage2Adapter = DetailStatusViewPage2Adapter(supportFragmentManager,lifecycle)
        binding.vp2Content.adapter = viewPage2Adapter

        TabLayoutMediator(binding.tlStatus, binding.vp2Content){tab, pos ->
            when (pos){
                0 -> {tab.text = "Chờ xác nhận"}
                1 -> {tab.text = "Chờ lấy hàng"}
                2 -> {tab.text = "Chờ giao hàng"}
                else -> {tab.text = "Đánh giá"}
            }
        }.attach()
    }
}