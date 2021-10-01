package com.pns.lk

class PnsDataManager {
    private var alertList: ArrayList<CallDetails> = ArrayList<CallDetails>()
    private var missedList: ArrayList<CallDetails> = ArrayList<CallDetails>()
    private var incomeList: ArrayList<CallDetails> = ArrayList<CallDetails>()
    private var inoutGoingList: ArrayList<CallDetails> = ArrayList<CallDetails>()
    private var limitDate: String? = null
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
    fun getIncomeList(): ArrayList<CallDetails> {
        return incomeList
    }
    fun getOutGoingList(): ArrayList<CallDetails> {
        return inoutGoingList
    }
    fun setLimitDate(date:String){
        this.limitDate = date
    }
    fun getLimitDate(): String? {
        return limitDate
    }
}