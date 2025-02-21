package com.example.raovat_app.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.raovat_app.fragments.ConfirmFragment
import com.example.raovat_app.fragments.DeliveryFragment
import com.example.raovat_app.fragments.EvaluateFragment
import com.example.raovat_app.fragments.PickupFragment

class DetailStatusViewPage2Adapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
       return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> {
                ConfirmFragment()
            }
            1 -> {
                PickupFragment()
            }
            2 -> {
                DeliveryFragment()
            }
            else -> {
                EvaluateFragment()
            }
        }
    }
}