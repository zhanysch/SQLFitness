package com.example.firebase.onBoard

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.firebase.R
import com.example.firebase.ui.main.MainActivity
import com.example.firebase.ui.oboard.OnBoardActivity
import kotlinx.android.synthetic.main.onboard_page.*

class OnBoardActivityTWO: AppCompatActivity() {
    private val list = arrayListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboard_page)
        setupViewpager()
        setupListener()
    }

    private fun setupListener() {
        OnviewPage.setOnPageChangeListener(object  : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                if (checktoPage(position)){
                    Btngo.text = "Pass to next Page"
                } else {
                    Btngo.text = "Next"
                }
            }
        })
       Btngo.setOnClickListener {
            if (checktoPage(OnviewPage.currentItem)) {
                startActivity(Intent(this, OnBoardActivity::class.java))
                finish()
            } else {
                OnviewPage.currentItem += 1
            }
       }
    }

    private fun checktoPage(position : Int) = position == list.size - 1

    private fun setupViewpager() {
        val adapter = OnBoardAdapter(supportFragmentManager)
        OnviewPage.adapter = adapter
        list.add(OnBoardFragment.getInstance(DataOnboard(R.drawable.ic_fitness2,getString(R.string.fitness),"")))
        list.add(OnBoardFragment.getInstance(DataOnboard(R.drawable.ic_improv, getString(R.string.run), "")))
        list.add(OnBoardFragment.getInstance(DataOnboard(R.drawable.run, getString(R.string.runn), "")))
        adapter.update(list)
        OnTabLT.setupWithViewPager(OnviewPage)
    }
}