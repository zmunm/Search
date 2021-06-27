package io.github.zmunm.search.binding

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import io.github.zmunm.search.R
import io.github.zmunm.search.getActivity
import java.util.Date

@BindingAdapter("android:visibility")
fun View.bindingVisibility(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("imageFromUrl")
fun ImageView.bindImageFromUrl(imageUrl: String?) {
    imageUrl ?: return
    if (imageUrl.isNotBlank()) {
        Glide.with(context)
            .load(imageUrl)
            .apply(RequestOptions().override(width, height))
            .fitCenter()
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(android.R.drawable.stat_sys_warning)
            .into(this)
    } else {
        setImageResource(android.R.drawable.stat_sys_warning)
    }
}

@BindingAdapter("title")
fun Toolbar.setTitleText(
    title: String?
) {
    title?.let(::setTitle)
}

@BindingAdapter("homeIndicator")
fun Toolbar.setToolbar(
    homeDrawable: Drawable?,
) {
    getActivity().let { activity ->
        activity.setSupportActionBar(this)
        activity.supportActionBar?.run {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(homeDrawable != null)
            homeDrawable?.let(::setHomeAsUpIndicator)
        }
    }
}

@BindingAdapter("html")
fun TextView.setHtml(html: String?) {
    val spannedHtml = if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
        @Suppress("DEPRECATION") Html.fromHtml(html ?: "")
    } else {
        Html.fromHtml(html ?: "", Html.FROM_HTML_MODE_LEGACY)
    }

    val buffer = SpannableString(spannedHtml)

    buffer.getSpans(0, buffer.length, URLSpan::class.java).forEach { span ->
        val end = buffer.getSpanEnd(span)
        val start = buffer.getSpanStart(span)
        buffer.setSpan(span, start, end, 0)
        buffer.setSpan(
            ForegroundColorSpan(Color.BLUE),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    text = buffer
    movementMethod = null
}

@BindingAdapter("date")
fun TextView.dateString(date: Date?) {
    if (date == null) {
        text = ""
        return
    }

    val gap = Date().time - date.time
    text = when (gap / ONE_DAY) {
        0L -> context.getString(R.string.today)
        1L -> context.getString(R.string.yesterday)
        else -> context.getString(R.string.date_format, date)
    }
}

private const val ONE_DAY: Long = 1000 * 60 * 60 * 24

@BindingAdapter("hasFocus")
fun EditText.setFocusChanged(hasFocus: Boolean) {
    if (hasFocus) requestFocus()
    else clearFocus()
}

@InverseBindingAdapter(attribute = "hasFocus", event = "focusChanged")
fun EditText.getFocusChanged(): Boolean = hasFocus()

@BindingAdapter("focusChanged")
fun EditText.setFocusChangedListener(listener: InverseBindingListener) {
    setOnFocusChangeListener { v, hasFocus ->
        listener.onChange()
    }
}
