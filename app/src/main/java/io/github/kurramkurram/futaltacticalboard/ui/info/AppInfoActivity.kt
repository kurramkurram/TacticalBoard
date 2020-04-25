package io.github.kurramkurram.futaltacticalboard.ui.info

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import io.github.kurramkurram.futaltacticalboard.BuildConfig
import io.github.kurramkurram.futaltacticalboard.R

class AppInfoActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_info)

        val version = findViewById<TextView>(R.id.version_text_view)
        version.text = BuildConfig.VERSION_NAME

        val ossButton = findViewById<TextView>(R.id.oss_text_view)
        ossButton.setOnClickListener(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.oss_text_view -> {
                val intent = Intent(this, OssLicensesMenuActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }


        return super.onOptionsItemSelected(item)
    }
}