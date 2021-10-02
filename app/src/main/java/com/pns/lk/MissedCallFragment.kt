package com.pns.lk

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
import java.util.*


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
        Utility.readMissedCall(view.context)
        missedList = PnsDataManager.instance?.getMissedList()!!
        lvSMS.adapter = CallListViewAdapter(view.context,missedList)
        lvSMS.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->
            val callOutgoingDuration = missedList[pos].contactNumber?.let {
                Utility.readOutgoingDurationFromContact(
                    view.context,
                    it
                )
            }
            val callIncomingDuration = missedList[pos].contactNumber?.let {
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
