package com.capgemini.todoreminder

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import java.util.*

class DBWrapper(context: Context) {

    val helper = DBHelper(context)
    val db = helper.writableDatabase

    fun saveReminder(title:String,calendar: Calendar,notes:String):Int{
        val timems =calendar.timeInMillis.toString()
        val cValues = ContentValues()

        //DATE TIME FORMATTING FROM CALENDER
        val hour =calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        var time=""
        if(hour<10) time = "0$hour:" else time = "$hour:"
        if(minute<10) time += "0$minute" else time += "$minute"
        val date = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)+1}/${calendar.get(Calendar.YEAR)}"

        cValues.put(DBHelper.CLM_TITLE,title)
        cValues.put(DBHelper.CLM_TIME,timems)
        cValues.put(DBHelper.CLM_TIME_VALUE,time)
        cValues.put(DBHelper.CLM_DATE_VALUE,date)
        cValues.put(DBHelper.CLM_NOTES,notes)
        return db.insert(DBHelper.TABLE_NAME,null,cValues).toInt()
    }

    fun deleteAll():Int{
        return db.delete(DBHelper.TABLE_NAME,null,null)
    }
    fun deleteAtID(id:Int):Int{
        return db.delete(DBHelper.TABLE_NAME,"${DBHelper.CLM_ID} = ?", arrayOf(id.toString()))
    }
    fun retrieveReminder() : Cursor {
        return db.query(DBHelper.TABLE_NAME,null,null,null,null,null,null)
    }
}