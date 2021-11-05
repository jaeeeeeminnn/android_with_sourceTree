package com.example.mywebbrowser

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 웹뷰 기본 설정
        webView.apply { 
            // 자바 스크립트 기능을 활성화
            settings.javaScriptEnabled = true
            // webViewClient 객체를 지정
            // 지정해야만 자체 웹 브라우저가 아니라 내가 만든 웹뷰에 페이지가 로딩됨
            webViewClient = WebViewClient()
        }

        // loadUrl 메소드를 이용하여 url을 전달
        // 웹뷰에 페이지 로딩
        webView.loadUrl("http://www.google.com")

        // 검색창 url 입력 + 소프트키보드 검색 클릭
        // 웹 페이지가 웹뷰에 표시되도록 하는 코드

        // edit Text의 setOnEditorActionListener{ 반응하는 뷰, 액션id, 이벤트 }
        // edit Text가 선택되고 글자가 입력될 때마다 호출됨. 
        urlEditText.setOnEditorActionListener { _, actionID, _ ->
            // actionId에 해당하는 상수값과 검색 버튼에 해당하는 상수값을 비교 
            if (actionID == EditorInfo.IME_ACTION_SEARCH) {
                // 현 actionid가 검색이면 검색창의 url을 웹뷰에 전달해 로딩
                webView.loadUrl(urlEditText.text.toString())
                // url을 웹뷰에 전달하면 true 반환 -> 이벤트 종료
                true
            } else {
                // 현 action이 검색이 아니라면 false 반환
                false
            }
        }

        // 컨텍스트 메뉴 등록
        // 컨텍스트 메뉴가 표시될 대상 뷰 지정
        registerForContextMenu(webView)
        // 이제 웹뷰를 길게 클릭하면 컨텍스트 메뉴가 표시됨.
    }

    // 액티비티에서 뒤로가기 키를 눌렀을 때 이벤트를 감지하고 재정의
    override fun onBackPressed() {
        // 웹뷰가 이전 페이지로 갈 수 있다면
        if (webView.canGoBack()) {
            // 이전 페이지로 이동
            webView.goBack()
        } else {
            // 이전 페이지로 갈 수 없다면 원래 동작을 수행 = 액티비티 종료
            super.onBackPressed()
        }
    }

    // 액티비티에서 메뉴 리소스 파일을 지정하여 사용자가 지정한 메뉴 표시
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // 메뉴 리소스를 액티비티의 메뉴로 표시하려는 메소드 
        menuInflater.inflate(R.menu.main, menu)
        // true를 반환하면 액티비티에 메뉴가 있다고 인식
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 메뉴 아이템으로 분기
        when (item?.itemId) {
            // 구글, 집 아이콘을 클릭하면 구글 페이지를 로딩
            R.id.action_google, R.id.action_home -> {
                webView.loadUrl("http://www.google.com")
                return true
            }
            // 네이버를 클리하면 네이버 페이지 로딩
            R.id.action_naver -> {
                webView.loadUrl("http://www.naver.com")
                return true
            }
            // 다음 클릭하면 다음 페이지 로딩
            R.id.action_daum -> {
                webView.loadUrl("http://www.daum.net")
                return true
            }
            // 연락처를 클릭하면 전화 앱을 열음.
            // 암시적 인텐트
            R.id.action_call -> {
                // 암시적 인텐트란 전화걸기/문자보내기/웹브라우저에서 열기/문자열 공유/이메일 보내기 등
                // 안드로이드에서 미리 정의된 인텐트를 말한다.

                // 인텐트를 정의하며 Intent 클래스에 정의된 액션 중 하나를 지정.
                // ACTION_DIAL은 전화 다이얼을 입력해주는 액션
                val intent = Intent(Intent.ACTION_DIAL)
                // 인텐트에 데이터를 지정.
                // 'tel:'로 시작하는 uri는 전화번호 국제표준
                // Uri.parse() 메소드로 감싼 Uri 객체를 데이터로 설정
                intent.data = Uri.parse("tel:031-123-4567")
                // resolveActivity 메소드는 인텐트를 수행하는 액티비티가 있는지를 검사하여 반환
                // null이 반환 -> 수행하는 액티비티가 없는 것 (전화 앱이 없는 태블릿 같은 경우)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
                return true
            }
            // 문자 보내기 코드 작성
            R.id.action_send_text -> {
                // 문자 보내기
                sendSMS("031-123-4567", webView.url!!)
                return true
            }
            // 이메일 보내기 코드 작성
            R.id.action_email -> {
                // 이메일 보내기
                email("test@example.com", "좋은 사이트", webView.url!!)
                return true
            }
        }
        // when 문에서 각 메뉴 처리를 끝내고 true를 반환함.
        // 처리하고자 하는 경우를 제외한 경우는 super 메소드를 호출하는 것이 보편적
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        // inflate 메소드를 사용하여 메뉴 리소스를 액티비티의 컨텍스트 메뉴로 사용하도록 설정
        menuInflater.inflate(R.menu.context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.action_share -> {
                // 페이지 공유
                share(webView.url!!)
                return true
            }
            R.id.action_brower -> {
                // 기본 웹 브라우저에서 열기
                browse(webView.url!!)
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}