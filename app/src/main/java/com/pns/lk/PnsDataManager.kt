package com.pns.lk

class PnsDataManager {
    private var alertList: ArrayList<CallDetails> = ArrayList<CallDetails>()
    private var missedList: ArrayList<CallDetails> = ArrayList<CallDetails>()

    companion object {
        private var manager: PnsDataManager? = null
        val instance: PnsDataManager?
            get() {
                if (manager == null) {
                    manager = PnsDataManager()
                }
                return manager
            }
    }

    fun getAlertList(): ArrayList<CallDetails> {
        return alertList
    }

    fun getMissedList(): ArrayList<CallDetails> {
        return missedList
    }
}