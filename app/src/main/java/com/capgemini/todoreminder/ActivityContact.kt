package com.capgemini.todoreminder

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ActivityContact : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
    }


    fun onButtonClicked(view: View) {
        when(view.id){
            R.id.smsB -> {
                Toast.makeText(this, "SMSing ....", Toast.LENGTH_LONG).show()
                startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("sms:12345678")))
            }
            R.id.callB -> {
                Toast.makeText(this, "Calling ....", Toast.LENGTH_LONG).show()
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:12345678")))
            }
            R.id.emailB -> {
                val send = Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:xyz@gmail.com"))
                Toast.makeText(this, "Emailing ....", Toast.LENGTH_LONG).show()
                startActivity(send)
            }
            R.id.visitB -> {
                Toast.makeText(this, "Visiting us ....", Toast.LENGTH_LONG).show()
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://capgemini.com")))
            }
        }
    }


}