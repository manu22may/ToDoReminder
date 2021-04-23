package com.capgemini.todoreminder

import android.app.*
import android.content.*
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.view_reminder_list.*
import java.util.*

/*
- ADD Reminder -> AddActivity
- Get data
- Buttons- ADD, CAncel
- Add clicked-
Status bar notification- new reminder added*/

//const val MY_BROADCAST_SCHEDULE_ACTION: String = "com.capgemini.todoreminder.action.scheduled"

class ActivityAdd : AppCompatActivity(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{
    //time variables
    private var y = 0
    private var M=0
    private var d=0
    private var h =0
    private var min=0
    private var s=0
    //user input variables
    private lateinit var calendar: Calendar
    private lateinit var title: String
    private lateinit var notes:String
//    val lreceiver =LocalReceiver() //inner class
//    val receiver =MyReminderReceiver() //outside class
    var beforeTime =0//shared preferences time

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        //----IF EDIT SELECTED----
        val intent =intent
        val type=intent.getStringExtra("type")?:"1"
        if(type=="2") {
            headingT.text="Edit Reminder"
            //---REMINDER LIST---
            /*val position = intent.getStringExtra("pos")?.toInt()?:0
            addTitleE.setText(Reminder.allReminder[position].title)
            tvTime.text = Reminder.allReminder[position].time
            tvDate.text = Reminder.allReminder[position].date
            if(Reminder.allReminder[position].details!=null)
                addDetailE.setText(Reminder.allReminder[position].details)*/

            //---CURSOR ADAPTER---
            val id =intent.getStringExtra("id")?:"-1"
            if(id!="-1")
            {
            val helper = DBHelper(this)
            val db = helper.writableDatabase
            val cursor = db.query(DBHelper.TABLE_NAME,null,"${DBHelper.CLM_ID} = ?",arrayOf(id),null,null,null)
                cursor.moveToFirst()
                addTitleE.setText(cursor.getString(cursor.getColumnIndex(DBHelper.CLM_TITLE)))
                tvTime.text = cursor.getString(cursor.getColumnIndex(DBHelper.CLM_TIME_VALUE))
                tvDate.text = cursor.getString(cursor.getColumnIndex(DBHelper.CLM_DATE_VALUE))
                addDetailE.setText(cursor.getString(cursor.getColumnIndex(DBHelper.CLM_NOTES))?:"")
            }
        }
/*        -----REGISTER RECEIVER-----
        val filter = IntentFilter(MY_BROADCAST_SCHEDULE_ACTION)
        registerReceiver(lreceiver,filter)
        registerReceiver(receiver, filter)*/
    }

/*    //----UNREGISTER RECEIVER----
    override fun onDestroy() {
        super.onDestroy()
//        unregisterReceiver(lreceiver)
//        unregisterReceiver(receiver)
    }*/

    /*----SHARED PREFERENCES---- */
    override fun onResume() {
        super.onResume()
        val pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        beforeTime=pref.getInt("time",0)
        Toast.makeText(this,"Time preference for reminder is $beforeTime minutes",Toast.LENGTH_SHORT).show()
    }

    //----CALLBACK FOR DIALOG FOR DATE TIME PICKER----
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        h=hourOfDay
        min=minute
        if(minute<10)
            tvTime.text="$hourOfDay:0$minute"
        else
            tvTime.text="$hourOfDay:$minute"
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        y=year
        M=month
        d=dayOfMonth
        tvDate.text ="$dayOfMonth/${month+1}/$year"
    }


    //----BUTTON CLICKED----
    fun buttonClicked(view: View) {
        when(view.id){
            R.id.addConfirmB -> {
                if (tvTime.text == "" || tvDate.text == "") {
                    Toast.makeText(this, "Enter a time", Toast.LENGTH_LONG).show()
                } else {
                    //---READ DATA FROM INTPUT
                        readInputFromUser()
//                    Reminder.addRem(Reminder(title, tvTime.text.toString(), tvDate.text.toString(), notes)) //store in list
                    sendNotification()
                    //---CALANDER INTENT---
                    savetoDB(title, calendar as GregorianCalendar,notes)
                    addEventToCalender()
                    //---START BROADCAST INTENT---
                    startBroadcast()
                    tvTime.text = ""
                    tvDate.text = ""
                    addDetailE.setText("")
                }
            }
            R.id.addCancelB -> finish()
            R.id.addTimeB -> {
                val dlg = MyDialog()
                val bundle = Bundle()
                bundle.putInt("type", 1)
                dlg.arguments = bundle
                dlg.show(supportFragmentManager, "Time")
            }
            R.id.addDateB -> {
                val dlg = MyDialog()
                val bundle = Bundle()
                bundle.putInt("type", 2)
                dlg.arguments = bundle
                dlg.show(supportFragmentManager, "Date")
            }
        }
    }


    //---READ INPUT AND STORE IN VARIABLES---
    private fun readInputFromUser() {
        val intent = intent
        val type = intent.getStringExtra("type") ?: "1"
        if (type == "2") {
            /*--REMINDER LIST--
            val position = intent.getStringExtra("pos")?.toInt() ?: 0
            Reminder.allReminder.removeAt(position)*/
            /*--SQLITE--*/
            val wrapper = DBWrapper(this)
            val id = intent.getStringExtra("id")
            if (id != null) wrapper.deleteAtID(id.toInt())
        }
        title = addTitleE.text.toString()
        notes = addDetailE.text.toString()
        calendar=GregorianCalendar(y, M, d, h, min, s)
    }


    //----SQLITE DATABASE----
    private fun savetoDB(title: String, calendar: Calendar, notes: String) {
        val wrapper = DBWrapper(this)
        val rowid=wrapper.saveReminder(title, calendar, notes)
        if(rowid.toInt()!=-1)
            Toast.makeText(this,"Data Saved in database",Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this,"Problem in insertion",Toast.LENGTH_SHORT).show()
    }


//    //----BROADCAST (implicit intent)----
//    private fun startBroadcast() {
//        val intent = Intent(MY_BROADCAST_SCHEDULE_ACTION)
//        intent.putExtra("title",addTitleE.text.toString())
//        val pi =PendingIntent.getBroadcast(this,0,intent,0)
//        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//        alarmManager.set(AlarmManager.RTC,calendar.timeInMillis-beforeTime*60*1000,pi)
//    }
    //----BROADCAST BACKGROUND (explicit Intent)----
    private fun startBroadcast() {
        val intent = Intent(this,MyReminderReceiver::class.java)//explicit
        intent.putExtra("title",title)
        val pi =PendingIntent.getBroadcast(this,0,intent,0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC,calendar.timeInMillis-beforeTime*60*1000,pi)
    }


/*    //----CALENDER INTENT----
    private fun addEventToCalender() {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE,addTitleE.text.toString())
            putExtra(CalendarContract.Events.DESCRIPTION,addDetailE.text.toString())
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.timeInMillis)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }*/

    //----CALENDER CONTENT PROVIDER----
    private fun addEventToCalender(){
        val wrapper = DBWrapper(this)
        val cursor = wrapper.retrieveReminder()
        cursor.moveToLast()
        val cr =contentResolver
        val cValues = ContentValues()

        cValues.put(CalendarContract.Events.CALENDAR_ID,3)
        cValues.put(CalendarContract.Events.TITLE,title)
        cValues.put(CalendarContract.Events.DTSTART,calendar.timeInMillis)
        cValues.put(CalendarContract.Events.DTEND,(calendar.timeInMillis+(60*60*1000)))
        cValues.put(CalendarContract.Events.DESCRIPTION,notes?:"none")
        cValues.put(CalendarContract.Events.EVENT_TIMEZONE,Calendar.getInstance().timeZone.id)

        cr.insert(CalendarContract.Events.CONTENT_URI,cValues)
    }



    //----NOTIFICATION----
    private fun sendNotification(str:String="New Reminder added : ${addTitleE.text.toString()}",id:Int=1) {
        val nManager =getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder: Notification.Builder = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//checks version
            val channel = NotificationChannel("test", "AndroidUI", NotificationManager.IMPORTANCE_DEFAULT)
            nManager.createNotificationChannel(channel)
            Notification.Builder(this, "test")
        }
        else{
            Notification.Builder(this)
        }
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        builder.setContentTitle("Reminder")
        builder.setContentText(str)
        builder.setAutoCancel(true)

        val intent = Intent(this, ActivityView::class.java)
        val pi =PendingIntent.getActivity(this, 1, intent, 0)
        builder.setContentIntent(pi)
        val myNotif = builder.build()

        nManager.notify(id, myNotif)
    }


/*        //----LOCAL RECEIVER CLASS----
    inner class LocalReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                MY_BROADCAST_SCHEDULE_ACTION->{
                    Toast.makeText(context,"Reminder occurred ${intent.getStringExtra("title")}",Toast.LENGTH_LONG).show()
                    sendNotification("Reminder occurred ${intent.getStringExtra("title")}",2)
                }
            }
        }
    }*/

}