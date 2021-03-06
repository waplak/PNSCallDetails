package com.pns.lk.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import com.pns.lk.dto.CallDetails
import com.pns.lk.dto.CallDuration
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class Utility {
    companion object {
        fun readAlert(context: Context) {
            PnsDataManager.instance?.getAlertList()?.clear()
            val cResolver = context.contentResolver
            val smsInboxCursor = cResolver.query(
                Uri.parse("content://sms/inbox"),
                null, null, null, null
            )
            val indexBody = smsInboxCursor!!.getColumnIndex("body")
            val indexAddress = smsInboxCursor.getColumnIndex("address")
            val indexDate = smsInboxCursor.getColumnIndex("date")
            if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return

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
                        alert.id = alertId
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
            smsInboxCursor.close()
        }

        fun readMissedCall(context: Context) {
            PnsDataManager.instance?.getMissedList()?.clear()
            val projection = arrayOf(
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.CACHED_NUMBER_TYPE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE,
                CallLog.Calls.DURATION
            )

            val sortOrder = CallLog.Calls.DATE + " DESC"
            val sb = StringBuffer()
            sb.append(CallLog.Calls.TYPE).append("=?")
                //.append(" and ").append(CallLog.Calls.IS_READ).append("=?")
            val cResolver = context.contentResolver
            val smsInboxCursor = cResolver.query(
                Uri.parse("content://call_log/calls"),
               // projection, null, null, sortOrder
                projection, sb.toString(), arrayOf(CallLog.Calls.MISSED_TYPE.toString()), sortOrder
            )

            val indexNumber: Int = smsInboxCursor!!.getColumnIndex(CallLog.Calls.NUMBER)
            val indexName: Int = smsInboxCursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val indexDate: Int = smsInboxCursor.getColumnIndex(CallLog.Calls.DATE)
            val indexDuration: Int = smsInboxCursor.getColumnIndex(CallLog.Calls.DURATION)

            if (indexNumber < 0 || !smsInboxCursor.moveToFirst()) return

            var missedId: Long = 0
            do {
                ++missedId
                val missed = CallDetails()
                missed.id = missedId
                missed.contactNumber = smsInboxCursor.getString(indexNumber)
                missed.contactName = smsInboxCursor.getString(indexName)
                missed.duration = convertSecondToHHMMString(smsInboxCursor.getInt(indexDuration))
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
            smsInboxCursor.close()
        }

        fun readIncomeCall(context: Context) {
            PnsDataManager.instance?.getIncomeList()?.clear()
            val projection = arrayOf(
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.CACHED_NUMBER_TYPE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE,
                CallLog.Calls.DURATION
            )

            val sortOrder = CallLog.Calls.DATE + " DESC"
            val sb = StringBuffer()
            sb.append(CallLog.Calls.TYPE).append("=?")
            //.append(" and ").append(CallLog.Calls.IS_READ).append("=?")
            val cResolver = context.contentResolver
            val smsInboxCursor = cResolver.query(
                Uri.parse("content://call_log/calls"),
                // projection, null, null, sortOrder
                projection, sb.toString(), arrayOf(CallLog.Calls.INCOMING_TYPE.toString()), sortOrder
            )

            val indexNumber: Int = smsInboxCursor!!.getColumnIndex(CallLog.Calls.NUMBER)
            val indexName: Int = smsInboxCursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val indexDate: Int = smsInboxCursor.getColumnIndex(CallLog.Calls.DATE)
            val indexDuration: Int = smsInboxCursor.getColumnIndex(CallLog.Calls.DURATION)

            if (indexNumber < 0 || !smsInboxCursor.moveToFirst()) return

            var incomeId: Long = 0
            do {
                ++incomeId
                val incomeCall = CallDetails()
                incomeCall.id = incomeId
                incomeCall.contactNumber = smsInboxCursor.getString(indexNumber)
                incomeCall.contactName = smsInboxCursor.getString(indexName)
                incomeCall.duration = convertSecondToHHMMString(smsInboxCursor.getInt(indexDuration))
                if (incomeCall.contactName == null) {
                    incomeCall.contactName = incomeCall.contactNumber
                }
                val date = smsInboxCursor.getString(indexDate)
                val strDate = dateFormatter(date,context)
                if (strDate != null) {
                    incomeCall.dateTime = strDate
                    PnsDataManager.instance?.getIncomeList()?.add(incomeCall)
                }
            } while (smsInboxCursor.moveToNext() && strDate != null)
            smsInboxCursor.close()
        }

        fun readOutgoingCall(context: Context) {
            PnsDataManager.instance?.getOutGoingList()?.clear()
            val projection = arrayOf(
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.CACHED_NUMBER_TYPE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE,
                CallLog.Calls.DURATION
            )

            val sortOrder = CallLog.Calls.DATE + " DESC"
            val sb = StringBuffer()
            sb.append(CallLog.Calls.TYPE).append("=?")
            //.append(" and ").append(CallLog.Calls.IS_READ).append("=?")
            val cResolver = context.contentResolver
            val smsInboxCursor = cResolver.query(
                Uri.parse("content://call_log/calls"),
                // projection, null, null, sortOrder
                projection, sb.toString(), arrayOf(CallLog.Calls.OUTGOING_TYPE.toString()), sortOrder
            )

            val indexNumber: Int = smsInboxCursor!!.getColumnIndex(CallLog.Calls.NUMBER)
            val indexName: Int = smsInboxCursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val indexDate: Int = smsInboxCursor.getColumnIndex(CallLog.Calls.DATE)
            val indexDuration: Int = smsInboxCursor.getColumnIndex(CallLog.Calls.DURATION)

            if (indexNumber < 0 || !smsInboxCursor.moveToFirst()) return

            var outGoingId: Long = 0
            do {
                ++outGoingId
                val outGoingCall = CallDetails()
                outGoingCall.id = outGoingId
                outGoingCall.contactNumber = smsInboxCursor.getString(indexNumber)
                outGoingCall.contactName = smsInboxCursor.getString(indexName)
                outGoingCall.duration = convertSecondToHHMMString(smsInboxCursor.getInt(indexDuration))
                if (outGoingCall.contactName == null) {
                    outGoingCall.contactName = outGoingCall.contactNumber
                }
                val date = smsInboxCursor.getString(indexDate)
                val strDate = dateFormatter(date,context)
                if (strDate != null) {
                    outGoingCall.dateTime = strDate
                    PnsDataManager.instance?.getOutGoingList()?.add(outGoingCall)
                }
            } while (smsInboxCursor.moveToNext() && strDate != null)
            smsInboxCursor.close()
        }
        fun readOutgoingDurationFromContact(context: Context,contactNo:String) : CallDuration {
            val projection = arrayOf(
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.CACHED_NUMBER_TYPE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE,
                CallLog.Calls.DURATION
            )

            val sortOrder = CallLog.Calls.DATE + " DESC"
            val sb = StringBuffer()
            sb.append(CallLog.Calls.TYPE).append("=?")
            .append(" and ").append(CallLog.Calls.NUMBER).append("=?")
            val cResolver = context.contentResolver
            val smsInboxCursor = cResolver.query(
                Uri.parse("content://call_log/calls"),
                projection, sb.toString(), arrayOf(CallLog.Calls.OUTGOING_TYPE.toString(),contactNo), sortOrder
            )

            val indexNumber: Int = smsInboxCursor!!.getColumnIndex(CallLog.Calls.NUMBER)
            val indexName: Int = smsInboxCursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val indexDate: Int = smsInboxCursor.getColumnIndex(CallLog.Calls.DATE)
            val indexDuration: Int = smsInboxCursor.getColumnIndex(CallLog.Calls.DURATION)
            if (indexNumber < 0 || !smsInboxCursor.moveToFirst()) return CallDuration()

            val outGoingDuration = CallDuration()
            outGoingDuration.contactNumber = smsInboxCursor.getString(indexNumber)
            outGoingDuration.contactName = smsInboxCursor.getString(indexName)
            if (outGoingDuration.contactName == null) {
                outGoingDuration.contactName = outGoingDuration.contactNumber
            }
            var outGoingDurationTime = 0
            do {
                val date = smsInboxCursor.getString(indexDate)
                val strDate = dateFormatter(date,context)
                if (strDate != null) {
                    outGoingDurationTime += smsInboxCursor.getInt(indexDuration)
                }
            } while (smsInboxCursor.moveToNext() && strDate != null)
            smsInboxCursor.close()
            outGoingDuration.outGoingDuration = convertSecondToHHMMString(outGoingDurationTime)
            return outGoingDuration
        }
        fun readIncomingDurationFromContact(context: Context,contactNo:String) : CallDuration {
            val projection = arrayOf(
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.CACHED_NUMBER_TYPE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE,
                CallLog.Calls.DURATION
            )

            val sortOrder = CallLog.Calls.DATE + " DESC"
            val sb = StringBuffer()
            sb.append(CallLog.Calls.TYPE).append("=?")
                .append(" and ").append(CallLog.Calls.NUMBER).append("=?")
            val cResolver = context.contentResolver
            val smsInboxCursor = cResolver.query(
                Uri.parse("content://call_log/calls"),
                projection, sb.toString(), arrayOf(CallLog.Calls.INCOMING_TYPE.toString(),contactNo), sortOrder
            )

            val indexNumber: Int = smsInboxCursor!!.getColumnIndex(CallLog.Calls.NUMBER)
            val indexName: Int = smsInboxCursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val indexDate: Int = smsInboxCursor.getColumnIndex(CallLog.Calls.DATE)
            val indexDuration: Int = smsInboxCursor.getColumnIndex(CallLog.Calls.DURATION)
            if (indexNumber < 0 || !smsInboxCursor.moveToFirst()) return CallDuration()

            val incomingDuration = CallDuration()
            incomingDuration.contactNumber = smsInboxCursor.getString(indexNumber)
            incomingDuration.contactName = smsInboxCursor.getString(indexName)
            if (incomingDuration.contactName == null) {
                incomingDuration.contactName = incomingDuration.contactNumber
            }
            var incomingDurationTime = 0
            do {
                val date = smsInboxCursor.getString(indexDate)
                val strDate = dateFormatter(date,context)
                if (strDate != null) {
                    incomingDurationTime += smsInboxCursor.getInt(indexDuration)
                }
            } while (smsInboxCursor.moveToNext() && strDate != null)
            incomingDuration.incomingDuration = convertSecondToHHMMString(incomingDurationTime)
            smsInboxCursor.close()
            return incomingDuration
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
            val sdfStrDate = SimpleDateFormat("yyyy-MM-dd")
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
                    PnsDataManager.instance?.setLimitDate("All Previous call history")
                    return sdfDate.format(callDate)
                }
                val timestamp = date.toLong()
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = timestamp
                val callDate = calendar.time
                val limitDate = limitCal.time
                val startDate = sdfStrDate.format(limitCal.time)
                if (limitDate.before(callDate)){
                    val sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    PnsDataManager.instance?.setLimitDate(
                        "Call History Data between $startDate To " +sdfDate.format(
                            Calendar.getInstance().time))
                    return sdfDate.format(callDate)
                }
                return null

            }
            val timestamp = date.toLong()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp
            val callDate = calendar.time
            val sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            PnsDataManager.instance?.setLimitDate("All Previous call history")
            return sdfDate.format(callDate)
        }

        private fun convertSecondToHHMMString(seconds: Int): String {
            val h = seconds / 3600
            val m = seconds % 3600 / 60
            val s = seconds % 60
            val sh = if (h > 0) "$h h" else ""
            val sm =
                (if (m in 1..9 && h > 0) "0" else "") + if (m > 0) if (h > 0 && s == 0) m.toString() else "$m min" else ""
            val ss =
                if (s == 0 && (h > 0 || m > 0)) "" else (if (s < 10 && (h > 0 || m > 0)) "0" else "") + s.toString() + " " + "sec"
            return sh + (if (h > 0) " " else "") + sm + (if (m > 0) " " else "") + ss
        }
    }

}