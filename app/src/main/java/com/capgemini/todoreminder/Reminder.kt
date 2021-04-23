package com.capgemini.todoreminder

import java.util.*
/*
----REMINDER DATA CLASS----
For using reminder list
 */
data class Reminder (var title:String? , var time:String,var date:String , var details:String?){
    companion object{
        val calender=Calendar.getInstance()
        val todayDate ="${calender.get(Calendar.DAY_OF_MONTH)}/${calender.get(Calendar.MONTH)}/${calender.get(Calendar.YEAR)}"
        val allReminder = mutableListOf<Reminder>(
                //basic few reminders added by default
                Reminder("Morning","08:00","$todayDate",null),
                Reminder("Noon","12:00","$todayDate",null),
                Reminder("Night","21:00","$todayDate",null))
        fun addRem(obj:Reminder){
            allReminder.add(obj)
        }
        fun show1():List<String>{ //show viewList
            val rStringList = mutableListOf<String>()
            for(r in allReminder)
            {
                rStringList.add("${r.title}   ${r.time} ( ${r.date} )")
            }
            return rStringList
        }
    }
}