package technited.minds.androidutils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(context: Context, tag: String?) {
    var sp: SharedPreferences = context.getSharedPreferences(tag, Context.MODE_PRIVATE)
    var edit: SharedPreferences.Editor = sp.edit()
    operator fun set(key: String?, value: String?) {
        edit.putString(key, value)
        edit.commit()
    }

    fun apply() {
        edit.apply()
    }

    operator fun get(key: String?): String? {
        return sp.getString(key, null)
    }

    fun clearAll() {
        sp.edit().clear().apply()
    }

}