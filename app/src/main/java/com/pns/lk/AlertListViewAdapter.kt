package com.pns.lk

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import java.util.*

class AlertListViewAdapter(context: Context,private val listAlert: ArrayList<CallDetails>) :BaseAdapter() {
    private val mInflator: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return listAlert.size
    }

    override fun getItem(position: Int): Any {
        return listAlert[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val vh: ListRowHolder
        if (convertView == null) {
            view = this.mInflator.inflate(R.layout.list_item, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }

        vh.contactName.text = listAlert[position].contactName
        vh.dateTime.text = listAlert[position].dateTime
        vh.callBackBt.setOnClickListener { v ->
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:" + listAlert[position].contactNumber)
            v.context.startActivity(callIntent)
        }
        vh.msgBackBt.setOnClickListener { v ->
            val uri = Uri.parse("smsto:" + listAlert[position].contactNumber)
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.putExtra("sms_body", "I will call you later")
            v.context.startActivity(intent)
        }

        return view
    }

    private class ListRowHolder(row: View?) {
        public val contactName: TextView = row?.findViewById(R.id.contact_name) as TextView
        public val dateTime: TextView = row?.findViewById(R.id.date_time) as TextView
        public val callBackBt: ImageButton = row?.findViewById(R.id.call_back) as ImageButton
        public val msgBackBt: ImageButton = row?.findViewById(R.id.msg_back) as ImageButton

    }
}