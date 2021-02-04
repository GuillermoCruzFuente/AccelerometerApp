package com.guillermo.accelerometer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        monitoringAccelerations.setOnClickListener {
            val goToMonitoring = Intent(this, ChartsActivity::class.java)
            startActivity(goToMonitoring)
        }
    }


}