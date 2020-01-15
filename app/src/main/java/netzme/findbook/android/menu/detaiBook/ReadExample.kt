package netzme.findbook.android.menu.detaiBook

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.webview.*
import netzme.findbook.android.R
import netzme.findbook.android.base.BaseActivity


class ReadExample : BaseActivity() {

    var url: String? =null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview)

        url = intent.getStringExtra("url")

        setToolbar(toolbar)
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.domStorageEnabled = true
        webView.settings.databaseEnabled = true
        webView.settings.allowFileAccessFromFileURLs = true
        webView.settings.allowUniversalAccessFromFileURLs = true
        webView.zoomIn()
        webView.zoomOut()

        setCookie()
        webView.loadUrl(url)
        load()
    }


    private fun setCookie(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val cookieManager =
                CookieManager.getInstance()
            cookieManager.setAcceptThirdPartyCookies(webView, true)
        }
        /*val cookieManager = CookieManager.getInstance()
        cookieManager.acceptCookie()

        val domain = "http://play.google.com"

        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(domain)

        cookieManager.setCookie(domain,"$cookieKey=$cookieValue")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView,true)
        }*/
    }
    private fun load() {

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
                webView.visibility = View.VISIBLE
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
