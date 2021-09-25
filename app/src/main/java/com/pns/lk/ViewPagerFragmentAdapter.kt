package com.pns.lk

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter


 class ViewPagerFragmentAdapter(manager: Fragment) :
    FragmentStateAdapter(manager) {
     private val titles = arrayOf("Incoming Calls", "Outgoing Calls")
    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return IncomingFragment()
            1 -> return OutgoingFragment()
        }
        return IncomingFragment()
    }

    override fun getItemCount(): Int {
        return titles.size
    }
}
