package sk.skwig.aisinator.feature.settings

import android.content.Context
import androidx.annotation.StringRes
import sk.skwig.aisinator.R
import java.security.InvalidKeyException

interface SettingsManager {
    var login: String
    var password: String
    var isTimetableShowingLectures: Boolean
    var isTimetableShowingSeminars: Boolean
    var isTimetableShowingCustom: Boolean
}

class SettingsManagerImpl(private val context: Context) : SettingsManager {

    val sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context)

    override var login: String
        get() = getString(PrefKey.LOGIN, "")
        set(value) = putString(PrefKey.LOGIN, value)

    override var password: String
        get() = getString(PrefKey.PASSWORD, "")
        set(value) = putString(PrefKey.PASSWORD, value)

    override var isTimetableShowingLectures: Boolean
        get() = getBoolean(PrefKey.TIMETABLE_SHOW_LECTURES, true)
        set(value) = putBoolean(PrefKey.TIMETABLE_SHOW_LECTURES, value)

    override var isTimetableShowingSeminars: Boolean
        get() = getBoolean(PrefKey.TIMETABLE_SHOW_SEMINARS, true)
        set(value) = putBoolean(PrefKey.TIMETABLE_SHOW_SEMINARS, value)

    override var isTimetableShowingCustom: Boolean
        get() = getBoolean(PrefKey.TIMETABLE_SHOW_CUSTOM, true)
        set(value) = putBoolean(PrefKey.TIMETABLE_SHOW_CUSTOM, value)

    @StringRes
    private fun getPrefRes(prefKey: PrefKey): Int {
        return when (prefKey) {
            PrefKey.LOGIN -> R.string.pref_key_login
            PrefKey.PASSWORD -> R.string.pref_key_password
            PrefKey.TIMETABLE_SHOW_LECTURES -> R.string.pref_key_timetable_show_lectures
            PrefKey.TIMETABLE_SHOW_SEMINARS -> R.string.pref_key_timetable_show_seminars
            PrefKey.TIMETABLE_SHOW_CUSTOM -> R.string.pref_key_timetable_show_custom
        }
    }

    private fun getBoolean(prefKey: PrefKey, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(context.getString(getPrefRes(prefKey)), defaultValue)
    }

    private fun putBoolean(prefKey: PrefKey, value: Boolean) {
        sharedPreferences.edit().putBoolean(context.getString(getPrefRes(prefKey)), value).apply()
    }

    private fun getString(prefKey: PrefKey, defaultValue: String): String {
        return sharedPreferences.getString(context.getString(getPrefRes(prefKey)), defaultValue)!!
    }

    private fun putString(prefKey: PrefKey, value: String) {
        sharedPreferences.edit().putString(context.getString(getPrefRes(prefKey)), value).apply()
    }

    private fun getInt(prefKey: PrefKey, defaultValue: Int): Int {
        return sharedPreferences.getInt(context.getString(getPrefRes(prefKey)), defaultValue)
    }

    private fun putInt(prefKey: PrefKey, value: Int) {
        sharedPreferences.edit().putInt(context.getString(getPrefRes(prefKey)), value).apply()
    }
}