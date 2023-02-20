package com.jangmyeongdal.activitylifecycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

lateinit var textView: TextView
var gameState: String? = null

class MainActivity : AppCompatActivity() {
    /**
     * 이 콜백은 시스템이 먼저 Activity를 생성할 때 실행되는 것 (필수적으로 구현)
     * @param savedInstanceState 이전 상태가 포함된 bundle객체 -> 처음 생성될 때는 null
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameState = savedInstanceState?.getString("GAME_STATE_KEY")
        Log.d("onCreate", gameState.toString())
        setContentView(R.layout.activity_main)
        gameState = "양희준"
        textView = findViewById(R.id.text_view)

    }

    /**
     * 이전의 activity 상태가 저장된 인스턴스가 있을 경우 호출되며 해당 인스턴스의 키 값을 획득하여 현재 Activity에 상태를 업데이트 해준다.
     * onCreate 이후 호출
     * */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textView.text = savedInstanceState?.getString("TEXT_VIEW_KEY")
        Log.d("onRestoreInstanceState", textView.text.toString())
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