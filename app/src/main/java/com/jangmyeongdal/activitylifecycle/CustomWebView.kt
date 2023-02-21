package com.jangmyeongdal.activitylifecycle

import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.webkit.WebViewFeature

/**
 * 웹뷰 클래스를 상속받음 커스텀웹뷰 클래스
 * 설정 및 사용법을 추가 변경함.
 * */
class CustomWebView: WebView {
    private val mContext : Context

    constructor(context: Context) : super(context) {
        mContext = context
        initWebView()
    }

    constructor(context: Context, attrs : AttributeSet) : super(context, attrs) {
        mContext = context
        initWebView()
    }

    constructor(context: Context, attrs : AttributeSet, defStyled : Int) : super(context, attrs, defStyled) {
        mContext = context
        initWebView()
    }

    private fun initWebView() {
        settings.run {
            javaScriptEnabled = true        // 자바스크립트 허용
            domStorageEnabled = true        // 로컬 스토리지 사용 여부를 설정하는 팝업창등을 '하루동안 보이지 않기' 기능등 사용에 필요
            javaScriptCanOpenWindowsAutomatically = false       // 팝업창을 띄울 경우가 있는데, 해당 속성을 추가해야 window.open() 제대로 동작
            loadsImagesAutomatically = true     // 웹뷰가 앱에 등록되어 있는 이미지 리소스를 자동으로 로드하도록 설정하는 속성
            useWideViewPort = true      // 화면 사이즈 맞추기 허용 여부
            loadWithOverviewMode = true     // 메타태그 허용 여부
            setSupportZoom(true)        // 화면 줌 허용 여부
            builtInZoomControls = false     // 화면 확대 축소 허용여부
            displayZoomControls = false     // 줌 컨트롤 없애기
            cacheMode = WebSettings.LOAD_NO_CACHE       // 웹뷰의 캐시 모드를 설정하는 속성으로써 5가지 모드
                                                        /*
                                                        (1) LOAD_CACHE_ELSE_NETWORK 기간이 만료돼 캐시를 사용할 수 없을 경우 네트워크를 사용합니다.
                                                        (2) LOAD_CACHE_ONLY 네트워크를 사용하지 않고 캐시를 불러옵니다.
                                                        (3) LOAD_DEFAULT 기본적인 모드로 캐시를 사용하고 만료된 경우 네트워크를 사용해 로드합니다.
                                                        (4) LOAD_NORMAL 기본적인 모드로 캐시를 사용합니다.
                                                        (5) LOAD_NO_CACHE 캐시모드를 사용하지 않고 네트워크를 통해서만 호출합니다.
                                                         */
            userAgentString = "app" // 웹에서 해당 속성을 통해 앱에서 띄운 웹뷰로 인지 할 수 있도록 한다
            defaultTextEncodingName = "UTF-8" // 인코딩 설정

            setSupportMultipleWindows(true)
        }

        if (!WebViewFeature.isFeatureSupported(WebViewFeature.START_SAFE_BROWSING)) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        var agent = settings.userAgentString;
        Log.d("initWebView", agent.toString())

        // 뷰 가속
        setLayerType(View.LAYER_TYPE_HARDWARE, null)

        webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {

                if (!(mContext as Activity).isFinishing) {
                    AlertDialog.Builder(mContext)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                result!!.confirm()
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show()
                }

                return true
            }
        }
        webViewClient = WebCallback()
    }

    inner class WebCallback: WebViewClient() {
        /**
         * 로딩이 시작될때
         * WebView에서 처음 한번만 호출되는 메서드 페이지 로딩이 시작된것을 알림
         */
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

            Log.d("onPageStarted", url.toString())
            Log.d("onPageStarted", favicon.toString())
        }

        /**
         * WebView가 주어진 URL로 지정된 리소스를 로드할 것이라고 알림
         * 페이지 로딩이 완료될 때까지 여러번 호출
         */
        override fun onLoadResource(view: WebView?, url: String?) {
            super.onLoadResource(view, url)
            //Log.d("onLoadResource", url.toString())
        }

        /**
         * 방문한 링크를 데이터베이스에 업데이트한다고 알림
         * url 변할때 마다
         */
        override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
            super.doUpdateVisitedHistory(view, url, isReload)
            Log.d("doUpdateVisitedHistory", url.toString())
        }

        /**
         * WebView에서 처음 한번만 호출되는 메서드
         * 페이지 로딩이 완료된 것을 알림
         */
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.d("onPageFinished", url.toString())
        }

        /**
         * 호스트 응용 프로그램에게 오류를 보고한다.
         * 웹뷰는 인터넷이 연결되지 않았을때 주소가 노출되는 단점이 있다. 이럴경우 url 주소를 보안상 노출되면 안되기 때문에 숨길경우 사용하면 유용
         */
        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)

        }

        /**
         * 새로운 url이 현재 webView에 로드되려고 할 때 호스트 응용프로그램에게 컨트롤을 대신할 기회를 준다.
         * post 요청은 호출 되지 않는다.
         */
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val url = request!!.url!!.toString()
            Log.d("OverrideUrlLoading", url)

            return if (url == "about:blank")
                super.shouldOverrideUrlLoading(view, request)
            else
                shouldOverrideLoading(view!!, request.url.toString(), request.method)
        }

        /**
         * 커스텀 함수로 여기서 url을 보고 분기 처리
         */
        private fun shouldOverrideLoading(webView : WebView, url : String, method : String) : Boolean {
            Log.d("customUrlLoading", url)
            return true
        }
    }

}