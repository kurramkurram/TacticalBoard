package io.github.kurramkurram.futaltacticalboard

import android.content.Context
import android.widget.Toast

fun Context.showToast(char: String): Unit =
    Toast.makeText(this, char, Toast.LENGTH_SHORT).show()