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
import java.text.SimpleDateFormat
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
        lvSMS.onItemClickListener = AdapterView.OnItemClickListener { arg0, arg1, pos, id ->
            var callOutgoingDuration = missedList[pos].contactNumber?.let {
                Utility.readOutgoingDurationFromContact(
                    view.context,
                    it
                )
            }
            var callIncomingDuration = missedList[pos].contactNumber?.let {
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
            if(PnsDataManager.instance?.getLimitDate()!=null){
                period.text = PnsDataManager.instance?.getLimitDate()
            }else{
                val sdfDate = SimpleDateFormat("yyyy-MM-dd")
                val limitCal = Calendar.getInstance()
                limitCal.add(Calendar.MONTH, -6)
                var startDate = sdfDate.format(limitCal.time)
                PnsDataManager.instance?.setLimitDate("Call History Data between "+startDate+" To "+sdfDate.format(Calendar.getInstance().time))
                period.text = PnsDataManager.instance?.getLimitDate()
            }

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
