package com.capgemini.todoreminder

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_settings.*
val PREF_NAME ="settings"
class ActivitySettings : AppCompatActivity() {

    val listSettings = listOf("0 min","Before 10 mins","Before 15 mins","Before 30 mins","Before 1 hour")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,listSettings)
        spinner.adapter=adapter
    }

    override fun onResume() {
        super.onResume()
        val pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val position=pref.getInt("position",0)
        spinner.setSelection(position)
    }


    fun buttonClicked(view: View) {
        when(view.id){
            R.id.settingsConfirmB->{
                val itemId=spinner.selectedItemId.toInt()
                saveSettings(itemId)
                Toast.makeText(this,"Settings changed: ${listSettings[itemId]}",Toast.LENGTH_SHORT).show()
                finish()
            }
            R.id.settingsCancelB->{finish()}
        }
    }


    //----SHARED PREFERENCES : WRITING----
    private fun saveSettings(itemId: Int) {
        val pref=getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val editor = pref.edit()
        val minutes = when(itemId){
            0-> 0
            1-> 10
            2-> 15
            3-> 30
            4-> 60
            else -> 0
        }
        editor.putInt("time",minutes)
        editor.putInt("position",itemId)
        editor.commit()
    }


}