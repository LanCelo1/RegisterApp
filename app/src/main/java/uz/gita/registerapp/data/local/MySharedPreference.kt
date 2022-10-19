package uz.gita.registerapp.data.local

import android.content.Context
import android.content.SharedPreferences
import uz.gita.registerapp.utils.SharedPreference

class MySharedPreference(context : Context) : SharedPreference(context) {
    var isUserActive : Boolean by BooleanPreference(false)
}