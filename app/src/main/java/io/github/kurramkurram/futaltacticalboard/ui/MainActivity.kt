package io.github.kurramkurram.futaltacticalboard.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_SYSTEM_OVERLAY_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!Settings.canDrawOverlays(applicationContext)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(
                        "package:$packageName"
                    )
                )
                startActivityForResult(
                    intent,
                    REQUEST_SYSTEM_OVERLAY_CODE
                )
                return
            }
        }
        val intent = Intent(this, FutsalCortActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SYSTEM_OVERLAY_CODE) {
            if (!Settings.canDrawOverlays(applicationContext)) {
                finish()
                return
            }
        }

        val intent = Intent(this, FutsalCortActivity::class.java)
        startActivity(intent)
        finish()
    }
}
