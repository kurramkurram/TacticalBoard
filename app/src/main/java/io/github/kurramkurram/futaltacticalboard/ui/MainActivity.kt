package io.github.kurramkurram.futaltacticalboard.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.github.kurramkurram.futaltacticalboard.R
import io.github.kurramkurram.futaltacticalboard.ui.animation.START_BUTTON_DURATION
import io.github.kurramkurram.futaltacticalboard.ui.animation.START_BUTTON_OFFSET
import io.github.kurramkurram.futaltacticalboard.ui.animation.blinkText

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startTextView = findViewById<TextView>(R.id.start_text)
        startTextView.setOnClickListener(this)
        blinkText(startTextView, START_BUTTON_DURATION, START_BUTTON_OFFSET)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.start_text -> {
                val intent = Intent(this, FutsalCortActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
