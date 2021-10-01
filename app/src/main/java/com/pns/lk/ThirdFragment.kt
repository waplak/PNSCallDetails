package com.pns.lk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


/**
 * A simple [Fragment] subclass.
 */
class ThirdFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_call_history, container, false)
        var viewPager = view.findViewById(R.id.view_pager) as ViewPager2
        var tabLayout = view.findViewById(R.id.tabs) as TabLayout
        viewPager.adapter = createCardAdapter();
        TabLayoutMediator(
            tabLayout, viewPager,
        ) { tab, position ->
            when (position) {
            0 -> { tab.text = "Incoming Calls"}
            1 -> { tab.text = "Outgoing Calls"}
            }
        }.attach()
        return view
    }

    private fun createCardAdapter(): ViewPagerFragmentAdapter? {
        return activity?.let { ViewPagerFragmentAdapter(requireParentFragment()) }
    }
}
