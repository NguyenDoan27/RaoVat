package com.example.raovat_app.adapters

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.raovat_app.fragments.AddNewFragment
import com.example.raovat_app.fragments.ProductStoreFragment
import com.example.raovat_app.fragments.RevenueFragment
import com.example.raovat_app.fragments.StoreConfirmFragment

class StoreViewPage2Adapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val idStore : String): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> {ProductStoreFragment().apply {
                arguments = Bundle().apply {
                    putString("id", idStore)
                }
            }}
            1 -> {RevenueFragment()}
            2 -> {
                StoreConfirmFragment().apply {
                    arguments = Bundle().apply {
                        putString("id", idStore )
                    }
                }
            }
            else -> {AddNewFragment().apply {
                arguments = Bundle().apply {
                    putString("id", idStore )
                }
            }}

        }

    }
}