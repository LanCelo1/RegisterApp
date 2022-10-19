package uz.gita.registerapp.utils

import android.app.Activity
import androidx.fragment.app.Fragment
import uz.gita.registerapp.MainActivity
import uz.gita.registerapp.R

fun Fragment.openScreenWithBackStack(fragment : Fragment){
    this.requireActivity().supportFragmentManager?.beginTransaction()
        .addToBackStack(null)
        .replace(R.id.container,fragment)
        .commit()
}
fun Fragment.openScreen(fragment : Fragment){
    this.requireActivity().supportFragmentManager?.beginTransaction()
        .replace(R.id.container,fragment)
        .commit()
}

fun Fragment.back(){
    this.requireActivity().supportFragmentManager.popBackStack()
}