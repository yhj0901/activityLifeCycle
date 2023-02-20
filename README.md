# activity lifecycle

- onCreate()
    - 이 콜백은 시스템이 먼저 Activity를 생성할 때 실행되는 것 (필수적으로 구현해야함)
    - Activity 생성되면 생성됨 상태가 됨
    - 이 메서드는 savedInstanceState 매개변수를 수신하는데 이는 활동의 이전 저장 상태가 포함된 bundle 객체이며 처음 생성될 때는 null이다.
        - onRestoreInstanceState(savedInstanceState: Bundle?) → 이전에 이 Activity 상태가 저장된 인스턴스가 있을 경우 호출되며 해당 인스턴스의 키값을 획득하여 현재 Activity에 상태를 업데이트 해준다.
        - onSaveInstanceState(outState: Bundle?) 콜백 메서드를 통해 강제로 종료되는 상황에 대한 대비가 가능 종료 직전에 해당 콜백 메서드가 호출되고 이때 상태값을 저장하여 onRestoreInstanceState(savedInstanceState: Bundle?) 메서드를 통해 복구 할수 있다.
        
        ```kotlin
        lateinit var textView: TextView
        
        // some transient state for the activity instance
        var gameState: String? = null
        
        override fun onCreate(savedInstanceState: Bundle?) {
            // call the super class onCreate to complete the creation of activity like
            // the view hierarchy
            super.onCreate(savedInstanceState)
        
            // recovering the instance state
            gameState = savedInstanceState?.getString(GAME_STATE_KEY)
        
            // set the user interface layout for this activity
            // the layout file is defined in the project res/layout/main_activity.xml file
            setContentView(R.layout.main_activity)
        
            // initialize member TextView so we can manipulate it later
            textView = findViewById(R.id.text_view)
        }
        
        // This callback is called only when there is a saved instance that is previously saved by using
        // onSaveInstanceState(). We restore some state in onCreate(), while we can optionally restore
        // other state here, possibly usable after onStart() has completed.
        // The savedInstanceState Bundle is same as the one used in onCreate().
        override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
            textView.text = savedInstanceState?.getString(TEXT_VIEW_KEY)
        }
        
        // invoked when the activity may be temporarily destroyed, save the instance state here
        override fun onSaveInstanceState(outState: Bundle?) {
            outState?.run {
                putString(GAME_STATE_KEY, gameState)
                putString(TEXT_VIEW_KEY, textView.text.toString())
            }
            // call superclass to save any view hierarchy
            super.onSaveInstanceState(outState)
        }
        ```
        
        - onSaveInstanceState()는 onDestroy() 메서드 호출되기 전에 호출, onRestoreInstanceState()는 onCreate() 메서드 호출 된 직후에 호출
- onStart()
    - Activity 시작됨 상태에 들어가면 시스템이 이 콜백을 호출
    - 사용자에게 표시 → Activity을 포그라운드에 보내 상호작용할 수 있도록 준비
        - 앱의 UI를 관리하는 코드를 초기화 함
    - 이 메서드는 매우 빠르게 완료되고, 시스템은 onResume() 메서드를 호출함
    - 여기서 Activity 상태는 재개됨 상태로 들어감
- onResume()
    - Activity 이 재개됨 상태에 들어가면 포그라운드에 표시되고 시스템이 onResume() 콜백을 호출함
    - 이 상태에 들어오면 앱이 이제 사용자와 상호 작용한다.
    - 어떤 이벤트가 발생하여 앱에서 포커스가 떠날 때까지 앱은 이상태에 머무른다.
        - 전화가 오거나, 사용자가 다른 activity로 이동, 기기 화면이 꺼지는 이벤트 등…
    - 이 상태에서 수명 주기 구성요소가 포그라운드에서 사용자에게 보이는 동안 실행해야 하는 모든 기능을 활성화 할 수 있다(예: 카메라 미리보기 시작)
        - 이 앱이 실행될때 사용하게 될 기능은 모드 이벤트를 받게 되고 그로 인해 기능을 활성화 할수 있는것인가? (확인 필요)
    - 방해가 되는 이벤트가 발생하면 Activity는 일시중지됨 상태에 들어가고 시스템은 onPause() 콜백을 호출
    - Activity가 일시중지됨 상태 → 재개됨 상태 로 돌아오면 시스템은 onResume() 메서드를 다시 한번 호출
        - onResume()을 구현하여 onPause() 중에 해제하는 구성요소를 초기화하고, Activity이 재개됨 상태로 전환될때마다 필요한 다른 초기화 작업도 수행해야 함
            
            ```kotlin
            class CameraComponent : LifecycleObserver {
            
                ...
            
            		// 카메라 컴포넌트에서 ON_RESUME를 수신할때 카메라에 액세스하는
                // 수명 주기 인식 구성요소를 초기화
                @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
                fun initializeCamera() {
                    if (camera == null) {
                        getCamera()
                    }
                }
            
                ...
            }
            ```
            
- onPause()
    - 시스템은 사용자가 Activity를 떠나는 것을 나타내는 첫 번째 신호로 이 메서드를 호출함.
        - 하지만 해당 활동이 항상 소멸되는 것은 아님
    - Activity가 포드라운드에 있지 않게 되었다는 것을 나타냄
        - 다만 사용자가 멀티 윈도우 모드에 있을 경우에는 여전히 표시 될 수도 있음)
    - onPause 메서드를 사용하여 Activity가 일시중지됨 상태일 때
        - 계속 실행(또는 적절히 계속 실행)되어서는 안되지만 잠시 후 다시 시작할 작업을 일시중지 또는 조정
        - Activity가 이 상태에 들어가는 이유
            - 일부 이벤트가 앱 실행을 방해(가장 일반적인 사례)
            - Android 7.0(API 수준 24) 이상에서는 여러 앱이 멀티 윈도우 모드에서 실행됨.
                - 언제든지 그중 하나의 앱(창)만 포커스를 가질수 있기 때문에 시스템이 그 외에 모든 다른 앱을 중지 시킴
            - 새로운 반투명 활동(예: 대화상자)이 열림 → 활동이 여전히 부분적으로 보이지만 포커스 상태가 아닌 경우에는 일시중지됨 상태로 유지(아래 그림처럼 뒤에 브라우저가 떠 있지만 앞에 다른 포커스가 유지되는 무언가가 있을경우)
                
                ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/96047112-3b5f-4f47-8994-9c6a788e73f0/Untitled.png)
                
    - Activity가 일시중지됨 상태로 전환하면 이 활동의 수명 주기와 연결된 모든 수명 주기 인식 구성요소는 ON_PAUSE 이벤트를 수진함.
        - 포그라운드에 있지 않을 때 실행할 필요가 없는 기능을 모두 정지 할수 있다.(예:카메라 미리보기 정지)
            
            ```kotlin
            class CameraComponent : LifecycleObserver {
            
                ...
            
                @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
                fun releaseCamera() {
                    camera?.release()
                    camera = null
                }
            
                ...
            }
            ```
            
    - onPause()는 아주 잠깐 실행되므로 저장 작업을 실행하기에는 시간이 부족할 수 있다.
        - 여기서는 사용자 데이터 저장, 네트워크 호출, 데이터베이스 트랜잭션을 실행하면 안됨
        - 이런 부하가 큰 종료 작업은 onStop() 상태일때 실행해야 한다.
- onStop()
    - Activity가 사용자에게 더 이상 표시 되지 않으면 중단됨 상태에 들어가고 시스템은 onStop() 콜백을 호출
        - 새로 시작된 Activity가 화면 전체를 차지할 경우 적용
    - 시스템은 Activity의 실행이 완료되어 종료된 시점에 onStop()을 호출할 수도 있다.
    - CPU를 비교적 많이 소모하는 종료 작업을 실행하면 된다.
        - 예를 들어 정보를 데이터베이스에 저장할 적절한 시기를 찾지 못했다면 onStop() 상태일 때 저장할 수 있다. → 샘플은 SQLite이지만 우리는 Room을 사용해야 한다. 추후 학습예정
            
            ```kotlin
            override fun onStop() {
                // call the superclass method first
                super.onStop()
            
                // save the note's current draft, because the activity is stopping
                // and we want to be sure the current note progress isn't lost.
                val values = ContentValues().apply {
                    put(NotePad.Notes.COLUMN_NAME_NOTE, getCurrentNoteText())
                    put(NotePad.Notes.COLUMN_NAME_TITLE, getCurrentNoteTitle())
                }
            
                // do this update in background on an AsyncQueryHandler or equivalent
                asyncQueryHandler.startUpdate(
                        token,     // int token to correlate calls
                        null,      // cookie, not used here
                        uri,       // The URI for the note to update.
                        values,    // The map of column names and new values to apply to them.
                        null,      // No SELECT criteria are used.
                        null       // No WHERE columns are used.
                )
            }
            ```
            
    - Activity가 중단됨 상태에 들어가면 Activity 객체는 메모리 안에 머무른다.
    - Activity가 정지됨 상태에서 다시 시작되어 사용자와 상호작용하거나, 실행을 종료하고 사라진다.
        - 활동이 다시 시작되면 시스템은 onRestart()를 호출
        - 실행을 종료하면 시스템은 onDestroy()를 호출
- onDestroy()
    - Activity가 소멸되기 전에 호출
    - 시스템은 다음중 하나에 해당할 때 이 콜백을 호출
        - (사용자가 활동을 완전히 닫거나 활동에서 finish()가 호출되어)Activity 가 종료되는 경우
        - 구성 변경(예: 기기 회전 또는 멀티 윈도우 모드)으로 인해 시스템이 일시적으로 활동을 소멸시키는 경우
    - onDestroy() 콜백은 이전의 콜백에서 아직 해제되지 않은 모든 리소스(예: onStop())를 해제 한다

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/2ec34886-c184-4d09-8158-cf55f23af24c/Untitled.png)

## 참조

- [https://developer.android.com/guide/components/activities/activity-lifecycle?hl=ko#kotlin](https://developer.android.com/guide/components/activities/activity-lifecycle?hl=ko#kotlin)
- 

[백그라운드에서 activity 시작에 대한 제한 사항](https://www.notion.so/activity-af92d15c771645ff8d4efb49555173eb)

[Activity 상태 및 메모리에서 제거](https://www.notion.so/Activity-9610afc323d64d60ad9df2d57a2e7567)

[임시 UI 상태 저장 및 복원](https://www.notion.so/UI-c212d0a42a314b1db78380911d59dedd)
