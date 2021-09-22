package com.pns.lk

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class MissedCallFragment : Fragment() {
    private var missedList = ArrayList<CallDetails>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_missed_call, container, false)
        val lvSMS: ListView = view.findViewById(R.id.lv_sms)
        missedList = PnsDataManager.instance?.getMissedList()!!
        lvSMS.adapter = CallListViewAdapter(view.context,missedList)
        return view
    }

}
