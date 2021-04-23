package com.capgemini.todoreminder

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MyReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
                val contentstr = "${intent.getStringExtra("title")} triggered"
                val  notifid =2


                //----SEND NOTIFICATION----
                val nManager =context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
                val builder: Notification.Builder = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//checks version
                    val channel = NotificationChannel("test", "Reminder Done", NotificationManager.IMPORTANCE_DEFAULT)
                    nManager.createNotificationChannel(channel)
                    Notification.Builder(context, "test")
                }
                else  Notification.Builder(context)

                builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                builder.setContentTitle("Reminder Done")
                builder.setContentText(contentstr)
                builder.setAutoCancel(true)

                val intent = Intent(context, ActivityView::class.java)
                val pi = PendingIntent.getActivity(context, 1, intent, 0)
                builder.setContentIntent(pi)
                val myNotif = builder.build()

                nManager.notify(notifid, myNotif)
    }
}