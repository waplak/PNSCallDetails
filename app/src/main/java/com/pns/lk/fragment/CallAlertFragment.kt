package com.pns.lk.fragment

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.pns.lk.*
import com.pns.lk.adapter.CallListViewAdapter
import com.pns.lk.dto.CallDetails
import com.pns.lk.util.PnsDataManager
import com.pns.lk.util.Utility
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
        Utility.readAlert(view.context)
        alertList = PnsDataManager.instance?.getAlertList()!!
        lvSMS.adapter = CallListViewAdapter(view.context,alertList)
        lvSMS.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->
            val callOutgoingDuration = alertList[pos].contactNumber?.let {
                Utility.readOutgoingDurationFromContact(
                    view.context,
                    it
                )
            }
            val callIncomingDuration = alertList[pos].contactNumber?.let {
                Utility.readIncomingDurationFromContact(
                    view.context,
                    it
                )
            }

            val cancel: ImageView
            val alertCustodial: View =
                LayoutInflater.from(view.context).inflate(R.layout.custom_dialog, null)
            val contactName : TextView = alertCustodial.findViewById(R.id.contactName)
            contactName.text = callIncomingDuration?.contactName
            val period : TextView = alertCustodial.findViewById(R.id.period)
            period.text = PnsDataManager.instance?.getLimitDate()
            val incomCall : TextView = alertCustodial.findViewById(R.id.incomCall)
            incomCall.text = callIncomingDuration?.incomingDuration
            val outCall : TextView = alertCustodial.findViewById(R.id.outCall)
            outCall.text = callOutgoingDuration?.outGoingDuration
            val alert = AlertDialog.Builder(view.context)

            alert.setView(alertCustodial)
            cancel = alertCustodial.findViewById<View>(R.id.cancel_button) as ImageView
            val dialog = alert.create()
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            cancel.setOnClickListener { dialog.dismiss() }

        }
        return view
    }

}
