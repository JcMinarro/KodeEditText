package com.jcminarro.kodeeditexts

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jcminarro.kodeedittext.sample.R.layout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        //PinEntryEditText(this)
    }
}
