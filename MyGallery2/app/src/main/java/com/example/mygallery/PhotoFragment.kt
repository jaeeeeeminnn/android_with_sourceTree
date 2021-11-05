package com.example.mygallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_photo.*

/*  2. 전자액자 구성

    a. 화면을 표시하는 프래그먼트 생성
    b. 이미지 로딩에 Glide 라이브러리 사용
    c. 화면 슬라이드를 구현하는 viewPager와 PagerAdapter 작성

    -> fragment가 생성되면서 imageView에 uri 경로에 있는 사진을 로딩.

 */

/*  Fragment

    사용자 인터페이스 모음.
    fragment 여러 개를 조합하여 액티비티 하나를 구성할 수 있다.
    fragment는 재사용도 가능하다.
    독자적인 생명주기를 가지고 있다.

    onCreate()      : fragment가 생성될 때 호출. 레이아웃 완성 전.
    onCreateView()  : 레이아웃 생성 전 호출. 레이아웃 완성 전.
    onViewCreate()  : 완성된 레이아웃 뷰를 백스택에 추가.
 */

// 클래스 선언 밖에 const 키워드 상수 정의하면 컴파일 시간에 결정되는 상수가 됨.
// 파일 내 어디서든 사용 가능.
// 컴파일 시간 상수 초기화는 기본형 (String, Int, Float, Double)만 가능.
private const val ARG_URI = "uri"

/**
 * A simple [Fragment] subclass.
 * Use the [PhotoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotoFragment : Fragment() {
    private var uri : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // fragment가 생성되어 onCreate() 메소드가 호출되면
        // ARG_URI 키에 저장된 uri값을 얻어서 변수에 저장.
        arguments?.let {
            uri = it.getString(ARG_URI)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // fragment에 표시될 뷰를 생성.
        // 액티비티가 아닌 곳에서 레이아웃 리소스를 가져오려면 LayoutInflater 객체의 inflate() 메소드를 사용.
        // 만들어 둔 fragment_photo.xml 파일을 inflate
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    // newInstance() 메소드를 이용해 fragment를 생성. (uri 값 인자로 전달)
    // 이 값은 Bundle 객체에 Bundle(키/값)으로 저장되고 arguments 속성에 저장됨.
    companion object {
        @JvmStatic
        fun newInstance(uri: String) =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_URI, uri)
                }
            }
    }

    /*  Glide

        이미지를 빠르고 부드럽게 로딩
        자동 메모리 관리

     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Glide.with(this)로 사진 사용준비를 하고
        // load() 메소드에 uri값을 인자로 주어 이미지를 부드럽게 로딩
        // into() 메소드로 imageView에 표시
        Glide.with(this).load(uri).into(imageView)
    }
}