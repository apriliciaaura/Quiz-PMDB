package com.example.sqlite1

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sqlite1.`object`.EmpModelClass
import com.example.sqlite1.helper.MyAdapter
import com.example.sqlite1.model.DatabaseHandler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //keyword yang berguna untuk memberitahu bahwa variable ini nantinya akan diinisialisasi sehingga variabel tersebut tidak akan null.
    //berguna untuk mencegah terjadinya nullPointerException yang bisa membuat app crash/force close.
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)
        button.setOnClickListener{
            startActivity(Intent(this, Home::class.java))
        }
    }

}