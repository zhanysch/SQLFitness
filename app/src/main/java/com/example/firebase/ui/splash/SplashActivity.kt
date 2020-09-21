package com.example.firebase.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.firebase.R
import com.example.firebase.data.PreferenceHelper
import com.example.firebase.onBoard.OnBoardActivityTWO
import com.example.firebase.ui.main.MainActivity
import com.example.firebase.ui.oboard.OnBoardActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            selectActivity()
        },3000)
        
    }

    private fun selectActivity(){
        if (PreferenceHelper.getIsFirtstLaunch()){
            startActivity(Intent(applicationContext, OnBoardActivityTWO:: class.java))
            finish()
        }
        else {
            startActivity(Intent(this, OnBoardActivity::class.java))
            finish()
        }
    }
}