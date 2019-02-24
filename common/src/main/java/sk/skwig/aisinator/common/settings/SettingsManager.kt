package sk.skwig.aisinator.common.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import sk.skwig.aisinator.common.R
import java.security.InvalidKeyException

interface SettingsManager {
    var login: String
    var password: String
}

class SettingsManagerImpl(private val context: Context) : SettingsManager {

    val sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context)

    override var login: String
        get() = getString(PrefKey.LOGIN, "")
        set(value) = putString(PrefKey.LOGIN, value)

    override var password: String
        get() = getString(PrefKey.PASSWORD, "")
        set(value) = putString(PrefKey.PASSWORD, value)

    @StringRes
    private fun getPrefRes(prefKey: PrefKey): Int {
        return when (prefKey) {
            PrefKey.LOGIN -> R.string.prefKey_login
            PrefKey.PASSWORD -> R.string.prefKey_password
        }
    }

    private fun getPrefKey(@StringRes prefRes: Int): PrefKey {
        return when (prefRes) {
            R.string.prefKey_login -> PrefKey.LOGIN
            R.string.prefKey_password -> PrefKey.PASSWORD
            else -> throw InvalidKeyException()
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