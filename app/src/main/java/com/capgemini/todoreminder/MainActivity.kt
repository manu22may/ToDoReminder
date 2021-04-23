package com.capgemini.todoreminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

/*ReminderAPP



-Add new reminder
-View reminders
-Edit reminder
- Delete reminder




REminder
- task title
task description
reminder date/time





- View Reminders
ListView
- List of reminders
- on selection of reminder
- Dialog- Edit/Delete
Edit- > display all details of reminder
Delete -> remove from list*/

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun onResume() {
        super.onResume()
        val wrapper = DBWrapper(this)
        val cursor=wrapper.retrieveReminder()
//        tvNum.text= Reminder.allReminder.size.toString()
        tvNum.text=cursor.count.toString()
    }


    fun buttonClicked(view: View) {
        lateinit var intent:Intent
        when(view.id){
            R.id.addB->{
                intent= Intent(this,ActivityAdd::class.java)
                startActivity(intent)
            }
            R.id.viewB->{
                intent= Intent(this,ActivityView::class.java)
                startActivity(intent)
            }
        }
    }


    //1.OPTIONS MENU
    val MENU_CONTACT=1 //unique +ve numbers
    val MENU_SETTINGS=2
    val MENU_EXIT=3
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0,MENU_CONTACT,1,"Contact us")
        menu?.add(0,MENU_SETTINGS,2,"Settings")
        menu?.add(0,MENU_EXIT,3,"Exit")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            MENU_CONTACT->{
                startActivity(Intent(this,ActivityContact::class.java))
                return true
            }
            MENU_SETTINGS->{
                startActivity(Intent(this,ActivitySettings::class.java))
                return true
            }
            MENU_EXIT->{
                //finish()
                //-----DISPLAY DIALOG-----
                val dlg = MyDialog()
                //----BUNDLE ARGUMENTS----
                val bundle =Bundle()
                bundle.putInt("type",3)
                dlg.arguments=bundle
                dlg.show(supportFragmentManager,"DialogueConfirm")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}