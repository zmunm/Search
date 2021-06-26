package io.github.zmunm.search.ui.activity

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import dagger.hilt.android.AndroidEntryPoint
import io.github.zmunm.search.Params
import io.github.zmunm.search.R
import io.github.zmunm.search.databinding.ActivityWebviewBinding
import io.github.zmunm.search.ui.base.BaseActivity

@AndroidEntryPoint
class WebViewActivity : BaseActivity<ActivityWebviewBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_webview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = (savedInstanceState ?: intent.extras) ?: return
        binding.title = bundle.getString(Params.TITLE)
        binding.webView.run {
            webViewClient = WebViewClient()
            settings.run {
                javaScriptEnabled = true
            }
            webChromeClient = WebChromeClient()
            val url = bundle.getString(Params.URL)

            if (url != null) {
                loadUrl(url)
            }
        }
    }
}
