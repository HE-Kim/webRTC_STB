package com.example.callapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_join.*
import java.util.*

class AddActivity : AppCompatActivity() {

    var firebaseRef = Firebase.database.getReference("pairing")
    var username =""
    var pid_str=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        username = intent.getStringExtra("username")!!

        val text: TextView = findViewById(R.id.text1)

        VerifyBtn.setOnClickListener {

            val random = Random()
            var pid = random.nextInt(9999)

           // var pid_str: String?


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

           // firebaseRef.child(username).child("Pairing").setValue(pid_str)
           // firebaseRef.child(username).child("user").setValue("none")

            firebaseRef.child(pid_str).child("STB").setValue(username)
            firebaseRef.child(pid_str).child("user").setValue("none")
            firebaseRef.child(pid_str).child("success").setValue("false")

            success_pair()
        }


    }
    private fun success() {
        Toast.makeText(this, "등록 완료", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, CallActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)

    }
    private fun success_pair() {
        firebaseRef.child(pid_str).child("success").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var value: String
                value= snapshot.value as String
                if(value=="true")
                {
                    success()
                }

            }
            override fun onCancelled(error: DatabaseError) {
                println("Failed to read value.")
            }
        })

    }




}