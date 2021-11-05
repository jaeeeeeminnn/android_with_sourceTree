package com.example.recyclerview3

import android.graphics.drawable.Drawable


// list_friends.xml에 들어갈 사람의 정보 (사진, 이름, 전화번호)를 저장할 클래스 정의
// adapter를 통해 전달되는 객체
class DataFriends (val imgProfile: Drawable?, val strName: String, val strPhone: String){

}