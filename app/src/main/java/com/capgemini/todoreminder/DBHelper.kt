package com.capgemini.todoreminder

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context):SQLiteOpenHelper(context,"reminderlist.db",null,1) {
    companion object{
        val CLM_ID = "_id"
        val TABLE_NAME ="Reminders"
        val CLM_TITLE ="Title"
        val CLM_TIME ="Timemilis"
        val CLM_TIME_VALUE ="TimeValue"
        val CLM_DATE_VALUE="DateValue"
        val CLM_NOTES ="Notes"
        val TABLE_QUERY = "create table $TABLE_NAME ($CLM_ID INTEGER PRIMARY KEY AUTOINCREMENT,$CLM_TITLE text,$CLM_TIME text,$CLM_TIME_VALUE text,$CLM_DATE_VALUE text,$CLM_NOTES text)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db?.execSQL(TABLE_QUERY)
        }
        catch (e:Exception){
            Log.e("DBHelper","ERROR creating table: ${e.message}")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

}