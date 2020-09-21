package com.example.firebase.ui.oboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebase.R
import com.example.firebase.data.PreferenceHelper
import com.example.firebase.onBoard.OnBoardActivityTWO
import com.example.firebase.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_on_board.*

class OnBoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board)
        setupListeners()
    }

    private fun setupListeners() {
        btn.setOnClickListener {
            PreferenceHelper.setIsFirtstLaunch() // сохраняет первый запуск
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}