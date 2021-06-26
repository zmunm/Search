package io.github.zmunm.search

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

fun <T> debounce(
    delayMs: Long = 500L,
    coroutineContext: CoroutineContext,
    block: (T) -> Unit,
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        debounceJob?.cancel()
        debounceJob = CoroutineScope(coroutineContext).launch {
            delay(delayMs)
            block(param)
        }
    }
}

fun hideKeyboard(activity: Activity) {
    (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(
            activity.currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS,
        )
}
