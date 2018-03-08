package com.example.examplethings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ref = FirebaseDatabase.getInstance().getReference("message")

        // Write a message to the database
        main_send.setOnClickListener {
            val text = main_edit.text.toString()
            ref.setValue(if (TextUtils.isEmpty(text)) "Empty message!" else text)
        }

        // Read from the database
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot?.getValue(String::class.java) ?: ""
                main_text.text = value
                Log.d(TAG, "Value is: " + value)
            }

            override fun onCancelled(error: DatabaseError?) {
                // Failed to read value
                error?.let {
                    main_text.text = it.message
                    Log.w(TAG, "Failed to read value.", it.toException())
                }
            }
        })
    }

}
