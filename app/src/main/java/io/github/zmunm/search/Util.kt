package io.github.zmunm.search

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

fun View.getActivity(): AppCompatActivity = context.getActivity()

fun Context.getActivity(): AppCompatActivity {
    var contextTemp = this
    while (contextTemp is ContextWrapper) {
        if (contextTemp is AppCompatActivity) {
            return contextTemp
        }
        contextTemp = contextTemp.baseContext
    }
    throw ActivityNotFoundException()
}

fun hideKeyboard(activity: Activity) {
    (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(
            activity.currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS,
        )
}
