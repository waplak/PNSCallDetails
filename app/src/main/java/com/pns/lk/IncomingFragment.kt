package com.pns.lk

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [IncomingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IncomingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var incomingList = ArrayList<CallDetails>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_incoming, container, false)
        val lvSMS: ListView = view.findViewById(R.id.lv_sms)
        Utility.readIncomeCall(view.context)
        incomingList = PnsDataManager.instance?.getIncomeList()!!
        lvSMS.adapter = CallListViewAdapter(view.context,incomingList)
        lvSMS.onItemClickListener = AdapterView.OnItemClickListener { arg0, arg1, pos, id ->
            var callOutgoingDuration = incomingList[pos].contactNumber?.let {
                Utility.readOutgoingDurationFromContact(
                    view.context,
                    it
                )
            }
            var callIncomingDuration = incomingList[pos].contactNumber?.let {
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
                PnsDataManager.instance?.setLimitDate("Call History Data between "+startDate+" To "+sdfDate.format(
                    Calendar.getInstance().time))
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment IncomingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            IncomingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}