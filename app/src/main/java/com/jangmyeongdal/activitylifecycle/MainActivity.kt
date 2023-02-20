package com.jangmyeongdal.activitylifecycle

import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

var gameState: String? = null

class MainActivity : AppCompatActivity() {
    /**
     * 이 콜백은 시스템이 먼저 Activity를 생성할 때 실행되는 것 (필수적으로 구현)
     * @param savedInstanceState 이전 상태가 포함된 bundle객체 -> 처음 생성될 때는 null
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 타이틀바 제거
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }

        gameState = savedInstanceState?.getString("GAME_STATE_KEY")
        Log.d("onCreate", gameState.toString())
        setContentView(R.layout.activity_main)

        // 웹뷰 설정
        val webView = findViewById<WebView>(R.id.webView)
        webView.settings.apply {
            javaScriptEnabled = true        //자바 스크립트 허용
            domStorageEnabled = true        // 로컬 스토리지 사용 여부를 설정하는 속 팝업창등을 '하루동안 보지 않기' 기능 사용에 필요
            javaScriptCanOpenWindowsAutomatically = false // 팝업창을 띄울 경우가 있는데, 해당 속성을 추가해야 window.open() 이 제대로 동작, 자바스크립트 새창도 띄우기 허용여부
            loadsImagesAutomatically = true // 웹뷰가 앱에 등록되어 있는 이미지 리소스를 자동으로 로드하도록 설정하는 속성
            useWideViewPort = true // 화면 사이즈 맞추기 허용 여부
            loadWithOverviewMode = true // 메타태그 허용 여부
            setSupportZoom(true) // 화면 줌 허용여부
            builtInZoomControls = false // 화면 확대 축소 허용여부
            displayZoomControls = false // 줌 컨트롤 없애기
            cacheMode = WebSettings.LOAD_NO_CACHE // 웹뷰의 캐시 모드를 설정하는 속성으로써 5가지 모드
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

        webView.apply {
            webViewClient = WebViewClient() // 하이퍼링크 클릭시 새창 띄우기 방지
            webChromeClient = WebChromeClient() // 크롬환경에 맞는 셋팅을 해줌. 특히, 알람등을 받기 위해서는 꼭 선언해주어야함. (alert같은 경우)
            loadUrl("https://m.naver.com")
        }

    }

    /**
     * 이전의 activity 상태가 저장된 인스턴스가 있을 경우 호출되며 해당 인스턴스의 키 값을 획득하여 현재 Activity에 상태를 업데이트 해준다.
     * onCreate 이후 호출
     * */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("onRestoreInstanceState", "onRestoreInstanceState 호출")
    }

    /**
     * 강제로 종료되는 상황에 대한 대비가 가능 종료 직전에 해당 콜백 메서드가 호출되고 이때 상태값을 저장
     * onDestroy 이전 호출
     * */
    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run {
            putString("GAME_STATE_KEY", gameState)
            putString("TEXT_VIEW_KEY", "양희준 테스트")
        }
        Log.d("onSaveInstanceState", outState.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        Log.d("onStart", "onStart 호출")
    }

    override fun onResume() {
        super.onResume()
        Log.d("onResume", "onResume 호출")
    }

    override fun onPause() {
        super.onPause()
        Log.d("onPause", "onPause 호출")
    }

    override fun onStop() {
        super.onStop()
        Log.d("onStop", "onStop 호출")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("onDestroy", "onDestroy 호출")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(":onRestart", "onRestart 호출")
    }
}