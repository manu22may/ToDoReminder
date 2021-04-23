package com.capgemini.todoreminder

import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_view.*
import kotlinx.android.synthetic.main.view_reminder_list.view.*
import java.util.*

class ActivityView : AppCompatActivity() {
//    lateinit var adapter:ReminderAdapterclass
    lateinit var adapter1:SimpleCursorAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        //SIMPLE CURSOR ADAPTER
        val wrapper = DBWrapper(this)
        val cursor = wrapper.retrieveReminder()
        cursor.moveToFirst()
        val args = arrayOf<String>(DBHelper.CLM_TITLE,DBHelper.CLM_TIME_VALUE,DBHelper.CLM_DATE_VALUE)
        val intargs = intArrayOf(R.id.titleT,R.id.timeT,R.id.dateT)
        adapter1 = SimpleCursorAdapter(this,R.layout.view_reminder_list,cursor,args,intargs,0)
        viewL.adapter=adapter1
        adapter1.notifyDataSetChanged()
        viewL.setOnItemClickListener { parent, view, position, id ->
            val MENU_EDIT = 1
            val MENU_DELETE = 2
            val pMenu = PopupMenu(this, view)
            val menu = pMenu.menu
            val m1=menu.add(0, MENU_EDIT, 0, "Edit")
            menu.add(0, MENU_DELETE, 1, "Delete")
            pMenu.show()

            pMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    MENU_EDIT -> {
                        Toast.makeText(this, "Edit", Toast.LENGTH_LONG).show()
                        val intent =Intent(this,ActivityAdd::class.java)
                        intent.putExtra("type","2")
                        intent.putExtra("id",id.toString())
                        startActivity(intent)
                        true
                    }
                    MENU_DELETE -> {
                        Toast.makeText(this, "Deleted Reminder", Toast.LENGTH_LONG).show()
                        //database
                        /*val helper = DBHelper(this)
                        val db = helper.writableDatabase
                        db.delete(DBHelper.TABLE_NAME,"${DBHelper.CLM_ID} = ?", arrayOf(id.toString()))*/
                        wrapper.deleteAtID(id.toInt())
                        Toast.makeText(this,"Deleted reminder",Toast.LENGTH_SHORT)
                        adapter1.notifyDataSetChanged()
                        true
                    }
                    else -> false
                }
            }
        }
    }


    fun onButtonClicked(view: View) {
        when(view.id)
        {
            R.id.deleteAllB->{
                //-----DISPLAY DIALOG-----
                val dlg = MyDialog()
                //----BUNDLE ARGUMENTS----
                val bundle =Bundle()
                bundle.putInt("type",4)
                dlg.arguments=bundle
                dlg.show(supportFragmentManager,"DeleteAllConfirm")
                // reminderListUpdate()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        adapter1.notifyDataSetChanged()
    }

/*    //----UPDATE ADAPTER ON RESUME----
    override fun onResume() {
        super.onResume()
        reminderListUpdate() //cursor update to list
        adapter.notifyDataSetChanged()

    }

    private fun reminderListAdapter(){
        //----CUSTOM REMINDER LIST ADAPTER----
        adapter = ReminderAdapterclass(this,R.layout.view_reminder_list,Reminder.allReminder)
        viewL.adapter=adapter

        //----POP UP MENU----
        viewL.setOnItemClickListener { parent, view, position, id ->
            val MENU_EDIT = 1
            val MENU_DELETE = 2
            val pMenu = PopupMenu(this, view)
            val menu = pMenu.menu
            val m1=menu.add(0, MENU_EDIT, 0, "Edit")
            menu.add(0, MENU_DELETE, 1, "Delete")
            if(!Reminder.allReminder[position].details.isNullOrEmpty())     //Show notes of reminder
                menu.add(0, 3, 2, "NOTES: ${Reminder.allReminder[position].details}")
            pMenu.show()

            pMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    MENU_EDIT -> {
                        Toast.makeText(this, "Edit", Toast.LENGTH_LONG).show()
                        val intent =Intent(this,ActivityAdd::class.java)
                        intent.putExtra("type","2")
                        intent.putExtra("pos",position.toString())
                        startActivity(intent)
                        true
                    }
                    MENU_DELETE -> {
                        Toast.makeText(this, "Deleted Reminder", Toast.LENGTH_LONG).show()
                        Reminder.allReminder.removeAt(position)
                        //database
                        val helper = DBHelper(this)
                        val db = helper.writableDatabase
                        db.delete(DBHelper.TABLE_NAME,"${DBHelper.CLM_ID} = ?", arrayOf(position.toString()))
                        adapter.notifyDataSetChanged()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun reminderListUpdate() {
        val wrapper =DBWrapper(this)
        val cursor= wrapper.retrieveReminder()
        cursor.moveToFirst()
        Reminder.allReminder.clear()
        if(cursor.count>0) {
            do {
                val title = cursor.getString(cursor.getColumnIndex(DBHelper.CLM_TITLE))
                val notes = cursor.getString(cursor.getColumnIndex(DBHelper.CLM_NOTES))
                val timedate = cursor.getString(cursor.getColumnIndex(DBHelper.CLM_TIME))
                val calendar = GregorianCalendar()
                calendar.timeInMillis = timedate.toLong()
                val hour =calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                var time=""
                if(hour<10) time = "0$hour:" else time = "$hour:"
                if(minute<10) time += "0$minute" else time += "$minute"
                val date = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)}/${calendar.get(Calendar.YEAR)}"
                Reminder.allReminder.add(Reminder(title, time, date, notes))
            } while (cursor.moveToNext())
        }
        adapter.notifyDataSetChanged()
    }*/


}


/*----CUSTOM ADAPTER CLASS----
    1.Override getItem
    2.getView
        2.1.get item
        2.2.check if context view exists or inflate layout
        2.3.map data and return view
 */
/*class ReminderAdapterclass(context: Context, val layoutRes:Int, val data:List<Reminder>): ArrayAdapter<Reminder>(context,layoutRes,data){
    override fun getItem(position: Int): Reminder? {
        return data[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val reminder = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(layoutRes,null) //.from(context) to create object
        view.titleT.setText(reminder?.title)
        view.timeT.setText(reminder?.time)
        view.dateT.setText(reminder?.date)
        return view
    }
}*/








