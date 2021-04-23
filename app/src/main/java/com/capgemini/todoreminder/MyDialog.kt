package com.capgemini.todoreminder

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import java.util.*


/*----DIALOG CLASS----
    1.Extend DialogFragment
    2.Override onCreateDialog
        2.1.builder =  AlertDialog.Builder(parentActivity)
        2.2.dlg = builder.create()
        2.3.return
 */
class MyDialog:DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val parent = activity!!
        lateinit var dlg:Dialog
        val dlgType = arguments?.getInt("type")
        val builder = AlertDialog.Builder(parent)
        when(dlgType){
            1-> {
                dlg = TimePickerDialog(parent,parent as TimePickerDialog.OnTimeSetListener,12,0,true)
            }
            2->{
                val calender=Calendar.getInstance()
                dlg=DatePickerDialog(parent, parent as DatePickerDialog.OnDateSetListener ,
                        calender.get(Calendar.YEAR),calender.get(Calendar.MONTH),calender.get(Calendar.DAY_OF_MONTH))

            }
            3->{
                //-----ALERT DIALOG-----
                builder.setTitle("Confirmation")
                builder.setMessage("Do you want  to exit the app?")
                builder.setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ -> parent.finish() })
                builder.setNegativeButton("No") { dialog, _ -> dialog.cancel()}//trailing lambda
                builder.setNeutralButton("Cancel") { dialog, _ ->dialog.cancel()}
                dlg=builder.create()
            }
            4->{
                //-----ALERT DIALOG-----
                builder.setTitle("Confirmation")
                builder.setMessage("Do you want  to delete all the reminders?")
                builder.setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                    val wrapper = DBWrapper(parent)
                    val num= wrapper.deleteAll()
                    Toast.makeText(parent,"DELETED ALL $num reminders",Toast.LENGTH_SHORT).show()
                })
                builder.setNegativeButton("No") { dialog, _ -> dialog.cancel()}//trailing lambda
                builder.setNeutralButton("Cancel") { dialog, _ ->dialog.cancel()}
                dlg=builder.create()
            }
        }
        return dlg
    }
}