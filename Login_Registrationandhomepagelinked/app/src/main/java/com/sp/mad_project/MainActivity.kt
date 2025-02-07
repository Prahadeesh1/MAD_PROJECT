package com.sp.mad_project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Reference the button
        val buttonToReport = findViewById<Button>(R.id.report_button)
        val post = findViewById<Button>(R.id.camera_icon)
        val search = findViewById<Button>(R.id.search_button)
        // Set a click listener for the button

        post.setOnClickListener { v: View? ->
            // Navigate to ReportActivity
            val intent = Intent(this@MainActivity, posts::class.java)
            startActivity(intent)
        }

        search.setOnClickListener { v: View? ->
            // Navigate to ReportActivity
            val intent = Intent(this@MainActivity, Search::class.java)
            startActivity(intent)
        }


        buttonToReport.setOnClickListener { v: View? ->
            // Navigate to ReportActivity
            val intent = Intent(this@MainActivity, ReportActivity::class.java)
            startActivity(intent)
        }
    }
}