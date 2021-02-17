package com.example.callapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_join.*
import java.util.*

class AddActivity : AppCompatActivity() {

    var firebaseRef = Firebase.database.getReference("users")
    var username =""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        username = intent.getStringExtra("username")!!

        val text: TextView = findViewById(R.id.text1)

        VerifyBtn.setOnClickListener {

            val random = Random()
            var pid = random.nextInt(9999)

            var pid_str: String?


            if (pid < 10) {
                text.setText("000$pid")
                pid_str = "000$pid"
            } else if (pid < 100) {
                text.setText("00$pid")
                pid_str = "00$pid"
            } else if (pid < 1000) {
                text.setText("0$pid")
                pid_str = "0$pid"
            } else {
                text.setText("$pid")
                pid_str = "$pid"
            }

            firebaseRef.child(username).child("info").child("Pairing").setValue(pid_str)
        }


    }
}