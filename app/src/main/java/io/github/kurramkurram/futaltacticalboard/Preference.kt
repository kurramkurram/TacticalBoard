package io.github.kurramkurram.futaltacticalboard

import android.content.Context

class Preference private constructor() {

    companion object {

        private const val FUTSAL_TACTICAL_PREFS = "futsal_tactical_prefs"
        const val KEY_HALF_CORT = "key_half_cort"
        const val KEY_BACKGROUND_RESOURCE_INDEX = "key_background_resource_index"

        const val KEY_PLAYER_NAME_PREFIX = "key_player_name_"
        const val KEY_PLAYER_NAME_COLOR_BLUE = "color_blue_"
        const val KEY_PLAYER_NAME_COLOR_RED = "color_red_"

        fun set(context: Context, key: String, value: Int) {
            val prefs = context.getSharedPreferences(FUTSAL_TACTICAL_PREFS, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putInt(key, value)
            editor.apply()
        }

        fun set(context: Context, key: String, value: String) {
            val prefs = context.getSharedPreferences(FUTSAL_TACTICAL_PREFS, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun set(context: Context, key: String, value: Boolean) {
            val prefs = context.getSharedPreferences(FUTSAL_TACTICAL_PREFS, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        fun get(context: Context, key: String, default: Int): Int {
            val prefs = context.getSharedPreferences(FUTSAL_TACTICAL_PREFS, Context.MODE_PRIVATE)
            return prefs.getInt(key, default)
        }

        fun get(context: Context, key: String, default: String): String? {
            val prefs = context.getSharedPreferences(FUTSAL_TACTICAL_PREFS, Context.MODE_PRIVATE)
            return prefs.getString(key, default)
        }

        fun get(context: Context, key: String, default: Boolean): Boolean {
            val prefs = context.getSharedPreferences(FUTSAL_TACTICAL_PREFS, Context.MODE_PRIVATE)
            return prefs.getBoolean(key, default)
        }
    }
}