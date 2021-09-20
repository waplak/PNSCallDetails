package com.pns.lk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class CallAlertFragment : Fragment() {
    private var alertList = ArrayList<CallDetails>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_call_alert, container, false)

        // setting List View for Messages
        val lvSMS: ListView = view.findViewById(R.id.lv_sms)
        alertList = PnsDataManager.instance?.getAlertList()!!
        lvSMS.adapter = AlertListViewAdapter(view.context,alertList)
        return view
    }

}
