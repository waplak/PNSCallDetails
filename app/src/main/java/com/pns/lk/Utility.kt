package com.pns.lk

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class Utility {
    companion object {
        fun readAlert(context: Context): ArrayList<CallDetails>? {
            PnsDataManager.instance?.getAlertList()?.clear()
            val cResolver = context.contentResolver
            val smsInboxCursor = cResolver.query(
                Uri.parse("content://sms/inbox"),
                null, null, null, null
            )
            val indexBody = smsInboxCursor!!.getColumnIndex("body")
            val indexAddress = smsInboxCursor.getColumnIndex("address")
            val indexDate = smsInboxCursor.getColumnIndex("date")
            if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return null

            var alertId: Long = 0
            do {
                val date = smsInboxCursor.getString(indexDate)
                val strDate = dateFormatter(date,context)
                if ("Alert" == smsInboxCursor.getString(indexAddress)) {
                    val line = smsInboxCursor.getString(indexBody)
                    val p = Pattern.compile("\\d{9,}")
                    val m = p.matcher(line)
                    while (m.find() && strDate != null) {
                        val alert = CallDetails()
                        ++alertId
                        alert.alertId = alertId
                        val number = m.group()
                        alert.contactNumber = number
                        val name = getContactName(number, context)
                        if (name != null) {
                            alert.contactName = name
                        } else {
                            alert.contactName = number
                        }
                        alert.dateTime = strDate
                        alert.count = 1
                        PnsDataManager.instance?.getAlertList()?.add(alert)

                    }
                }
            } while (smsInboxCursor.moveToNext() && strDate != null)
            return PnsDataManager.instance?.getAlertList()
        }

        fun readMissedCall(context: Context): ArrayList<CallDetails>? {
            PnsDataManager.instance?.getMissedList()?.clear()
            val projection = arrayOf(
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.TYPE
            )

            val sortOrder = CallLog.Calls.DATE + " DESC"
            val sb = StringBuffer()
            sb.append(CallLog.Calls.TYPE).append("=?")
                //.append(" and ").append(CallLog.Calls.IS_READ).append("=?")
            val cResolver = context.contentResolver
            val smsInboxCursor = cResolver.query(
                Uri.parse("content://call_log/calls"),
                projection, sb.toString(), arrayOf(CallLog.Calls.MISSED_TYPE.toString()), sortOrder
            )

            val indexNumber: Int = smsInboxCursor!!.getColumnIndex(CallLog.Calls.NUMBER)
            val indexName: Int = smsInboxCursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val indexDate: Int = smsInboxCursor.getColumnIndex(CallLog.Calls.DATE)

            if (indexNumber < 0 || !smsInboxCursor.moveToFirst()) return null

            var missedId: Long = 0
            do {
                ++missedId
                val missed = CallDetails()
                missed.alertId = missedId
                missed.contactNumber = smsInboxCursor.getString(indexNumber)
                missed.contactName = smsInboxCursor.getString(indexName)
                if (missed.contactName == null) {
                    missed.contactName = missed.contactNumber
                }
                val date = smsInboxCursor.getString(indexDate)
                val strDate = dateFormatter(date,context)
                if (strDate != null) {
                    missed.dateTime = strDate
                    PnsDataManager.instance?.getMissedList()?.add(missed)
                }
            } while (smsInboxCursor.moveToNext() && strDate != null)

            return PnsDataManager.instance?.getMissedList()
        }

        private fun getContactName(phoneNumber: String?, context: Context): String? {
            val uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber)
            )
            val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
            var contactName: String? = null
            val cursor = context.contentResolver.query(uri, projection, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    contactName = cursor.getString(0)
                }
                cursor.close()
            }
            return contactName
        }

        @SuppressLint("SimpleDateFormat")
        private fun dateFormatter(date: String, context: Context): String? {
            val sharedPreferences = context.getSharedPreferences("PNS_PREF", AppCompatActivity.MODE_PRIVATE)
            if(sharedPreferences.contains("durationIndex")){
                val index = sharedPreferences.getInt("durationIndex", 0)
                val limitCal = Calendar.getInstance()
                if (index==0){
                    limitCal.add(Calendar.MONTH, -1)
                }else if(index==1){
                    limitCal.add(Calendar.MONTH, -3)
                }else if(index==2){
                    limitCal.add(Calendar.MONTH, -6)
                }else{
                    val timestamp = date.toLong()
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = timestamp
                    val callDate = calendar.time
                    val sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    return sdfDate.format(callDate)
                }
                val timestamp = date.toLong()
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = timestamp
                val callDate = calendar.time
                val limitDate = limitCal.time
                if (limitDate.before(callDate)){
                    val sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    return sdfDate.format(callDate)
                }
                return null;

            }
            val timestamp = date.toLong()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp
            val callDate = calendar.time
            val sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return sdfDate.format(callDate)
        }
    }
}