package com.example.ticketbox

import android.graphics.drawable.Drawable

/*  MovieInfo.kt

    영화 정보 (커버 이미지, 영화 제목, 가격, 감독, 장르)를 저장하기 위한 클래스
    Adapter를 통해 MainActivity에서는 MovieInfo 객체였던 것이
    RecyclerAdaptor에서는 viewHolder가 된다.
    다시말해 Adapter를 통해서 recycler view의 정보 전달을 수행한다.
 */
class MovieInfo (val imgProfile: Drawable?, val strName: String, val intPrice: Int, val strDirec: String, val strgenr: String) {
}