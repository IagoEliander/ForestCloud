package com.forestcloud.forestcloud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Home : AppCompatActivity() {
    private lateinit var buttonLogin: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        buttonLogin = findViewById(R.id.Login)
        buttonLogin.setOnClickListener{
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
        }



    }
}