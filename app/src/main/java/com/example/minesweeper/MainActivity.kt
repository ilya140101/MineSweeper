package com.example.minesweeper

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {

        // super.onBackPressed()
        AlertDialog.Builder(this)
            .setTitle(R.string.exit_title)
            .setMessage(R.string.exit_text)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->

            }
            .show()
    }

}