package com.example.mygallery

import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import layout.MyPagerAdapter
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.util.jar.Manifest
import kotlin.concurrent.timer

/* MyGallery 구현 순서

    1. 프로바이더 사용
    2. 전자액자 구성
    3. 슬라이드 쇼 구현

    - Content Provider  : 사진 정보를 얻을 때 사용
    - Fragment          : UI의 일부 표현
    - ViewPager         : fragment 여러개를 좌우로 슬라이드해 넘길 수 있게 함.
    - FragmentStatePagerAdapter : 페이지가 많을 때 유용한 뷰페이저용 어댑터
    - timer             : 일정 시간 간격으로 반복 동작
 */

class MainActivity : AppCompatActivity() {

    private val REQUEST_READ_EXTERNAL_STORAGE = 1000

    /*  프로바이더로 사진 정보 가져오는 순서

        1. 프로바이더로 기기의 사진 경로얻기
        2. manifest에 외부 저장소 읽기 권한 추가
        3. 권한 확인
        4. 권한 요청
        5. 사용 권한 요청 응답 처리
        6. 앱 실행

     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
            외부저장소 접근은 위험권한이므로 실행마다 사용자의 허가를 확인해야 함.

            ContextCompat.checkSelfPermission() : 권한 있는지 확인하는 메소드
            PERMISSION_GRANTED  : 앱 권한 O
            PERMISSOIN_DENIED   : 앱 권한 X

            앱 권한 확인 {
                권한 허용 x {
                    과거 권한 거부 o {
                        확인 버튼 {
                            권한 요청
                        }
                        거절 버튼 { }
                    }
                }
                권한 허용 o {
                    권한 요청
                }
            }

         */
        // 앱 권한 확인
        // 권한이 허용되지 않음.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 사용자에게 명시적으로 권한 요청
            // shouldShowRequestPermissionRationale() : 과거 권한 요청 거부했는지 반환. (true : 거절했었음)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // 과거 거절 당했다면 메시지를 보내서 권한 설정하도록 알림
                alert ("사진 정보를 얻으려면 외부 저장소 권한이 필수로 필요합니다.", "권한이 필요한 이유") {
                    yesButton {
                        // 권한 요청함.
                        // REQUEST_READ_EXTERNAL_STORAGE : 적당한 정수값 (권한 요청에 대한 결과 분기처리에 활용)
                        ActivityCompat.requestPermissions(this@MainActivity,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE)
                    }
                    noButton {  }
                }.show()
            }
            // 과거 거절 경험이 없다면
            else {
                // 바로 권한 요청
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE)
            }
        } else {
            // 권한이 이미 허용됨.
            getAllPhotos()
        }
    }

    /*  onRequestPermissionResult()

        사용자가 권한을 요청하면 호출되고 사용자의 응답을 전달하는 메소드
        응답 결과를 확인하여 사진 정보를 가져오거나 Toast 메시지를 표시하는 코드를 작성한다.

     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // requestCode  : 요청한 권한 코드
        // grantResults : 요청 결과 (배열)
        // 지금은 하나의 권한만 요청했으므로 0번 index만 확인.
        // grantResults[0] = PERMISSION_GRANTED 또는 PERMISSION_DENIED
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            // 권한 요청에 대한 결과 분기 처리에 사용을 하는 거.
            REQUEST_READ_EXTERNAL_STORAGE -> {
                // 권한 허용됨
                if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getAllPhotos()
                }
                // 권한 거부
                else {
                    toast("권한 거부 됨")
                }
                return
            }
        }
    }

    private fun getAllPhotos() {
        // 모든 사진 정보 가져오기
        // 외부저장소 접근 권한 필요 (MediaStore 객체 비활성 -> 활성)
        // contentResolver객체의 query() 메소드는 인자가 5개.
        val cursor: Cursor? = contentResolver.query(
            // 첫 인자 : 어떤 데이터를 가져오는지에 대한 URI
            // 사진 정보는 외부저장소에 있기 때문에 EXTERNAL_CONTENT_URI로 설정
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            // 둘 인자 : 어떤 항목의 데이터를 가져올 것인지 String 비열로 지정.
            // 가져올 데이터의 구조를 잘 모른다면 null ( 모든 항목을 가져옴)
            null,
            // 셋 인자 : 데이터를 가져올 조건 지정
            // 전체 데이터를 가져올 경우엔 null
            null,
            // 세번째 인자와 조합하여 조건을 지정
            // 사용하지 않는다면 null
            null,
            // 정렬 방법 지정
            // DATE_TAKEN으로 사진이 찍힌 날짜의 내림차순으로 정렬
            MediaStore.Images.ImageColumns.DATE_TAKEN+ " DESC")

        // fragment를 item으로 하는 ArrayList를 생성.
        // 사진을 Cursor 객체로부터 가져올 때마다 PhotoFragment.newInstance(uri)로 fragment 생성하면서
        // fragments 리스트에 추가
        val fragments = ArrayList<Fragment>()

        /*  Cursor 클래스

            사진정보를 담고 있는 객체.
            내부적으로 데이터를 이동하는 포인터를 가지고 있기 때문에
            moveToNext()로 다음 정보로 이동하고 결과를 true로 반환

            while문으로 전체 순회를 할 수도 있다.
            사진이 없다면 Cursor 객체는 null이다.

         */
        // cursor에 사진이 있다면
        if (cursor != null) {
            while (cursor.moveToNext()) {
                // 사진 경로 uri 가져오기
                // 사진 경로가 저장된 DB의 컬럼명은 DATA 상수에 있음.
                // getColumnIndexOrThrow() 메소드로 해당 컬럼이 몇 번째 인덱스인지 알 수 있다.
                // getString() 메소드에 위에서 얻은 index를 전달하면 해당하는 데이터를 String 타입으로 반환
                // 즉, uri(사진을 저장한 위치의 경로)를 얻을 수 있다.
                val uri: String = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                Log.d("MainActivity", uri)
                // fragment를 생성하면서 리스트에 추가.
                fragments.add(PhotoFragment.newInstance(uri))
            }
            // Cursor 객체를 더이상 사용하지 않을 때는 메모리 누수 방지를 위해 close() 해야 함.
            // 메모리 누수 : 메모리가 해제되지 않는 상황이 지속되는 것.
            // 내버려두면 race condition -> dead lock 이 될 수 있음.
            cursor.close()
        }

        /*  Adapter

            MyPagerAdapter를 생성하면서 FragmentManager를 생성자의 인자로 전달.
            getSupportFragmentManager() : FragmentManager 가져옴
            supportFragmentManager()    : FragmentManager 가져옴
            updateFragments()           : 생성된 Adapter에 fragment list를 전달.

            -> adapter를 viewPager(activity_main.xml의 viewPager 블록
         */
        val adapter = MyPagerAdapter(supportFragmentManager, this.lifecycle)
        adapter.updateFragments(fragments)
        viewPager.adapter = adapter
        // 여기까지 좌우로 슬라이드가 가능한 앨범 구현

        /*  3. 슬라이드 쇼 구현

            1. timer로 3초마다 코드 실행하기
            2. runOnUiThread로 timer내부에서 UI조작하기

         */
        // timer로 3초마다 코드 실행하기
        timer(period = 3000) {
            // 이미 inflate된 ui를 다시 그림.
            runOnUiThread {
                // viewPager의 현재 item이 list의 끝이 아니라면
                if (viewPager.currentItem < adapter.itemCount - 1) {
                    // 다음 position의 item으로 넘어감.
                    viewPager.currentItem = viewPager.currentItem + 1
                } else {
                    // list의 끝에 도달했으면 처음부터 시작.
                    viewPager.currentItem = 0
                }
            }
        }
    }
}