package com.example.roomrecyclerexample.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomrecyclerexample.Entity.Course
import com.example.roomrecyclerexample.R
import com.example.roomrecyclerexample.activity.MainActivity
import com.example.roomrecyclerexample.adapter.CourseAdapter
import com.example.roomrecyclerexample.database.CourseDatabase
import com.example.roomrecyclerexample.model.CourseModel
import com.example.roomrecyclerexample.rest.HttpTask
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.fragment_course.*
import kotlinx.android.synthetic.main.fragment_course.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CourseFragment : Fragment() {
    // 어뎁터 생성하고 url로 http 응답 받아서 그걸 inflate
    private lateinit var mAdapter : CourseAdapter

    private val START_POINT = 1
    private val END_POINT = 40

    lateinit var courseDb : CourseDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // RoomDatabase 상속 인스턴스 생성
        courseDb = CourseDatabase.getInstance(this.requireContext())!!

        var courseList: ArrayList<Course> = arrayListOf()

        val view = inflater.inflate(R.layout.fragment_course, container, false)

        CoroutineScope(Dispatchers.Main).launch {
            // entity 준비
            withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                for (i in START_POINT..END_POINT) {
                    Log.d("COURSEID", "$i")
                    // i는 코스Id니깐 getUrlorNull을 이용해서 코스name을 모조리 가져오면 됨.
                    val response = HttpTask.getJsonResponse(i.toString())
                    if (HttpTask.checkNoData(response)) {
                        val dataArray = HttpTask.getDataArray(response)
                        val courseName: String = HttpTask.getCourseName(dataArray)

                        // entity 를 만듬.
                        val course = Course(i, courseName)
                        courseList.add(course)
                    }
                }
            }

            // 싱글톤 사용
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                // db에 entity insert (중복 대체 : REPLACE)
                for (c in courseList) {
                    courseDb.CourseDao().insert(c)
                }
            }
            var dbList: List<Course> = listOf()
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                dbList = courseDb.CourseDao().getAll() as List<Course>
            }

            if (dbList.isEmpty()) {
                Log.d("DBLIST", "is Empty")
            } else {
                Log.d("DBLIST", "$dbList")
            }

            mAdapter = CourseAdapter()

            view.framgent_list.layoutManager = LinearLayoutManager(requireContext())
            view.framgent_list.adapter = mAdapter

            // spinner
            val spinner = view.fragment_spinner
            val sList : ArrayList<String> = arrayListOf()
            for (i in 0 until dbList.size) {
                sList.add(dbList[i].courseName)
            }
            val sAdapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, sList)
            spinner.adapter = sAdapter

            spinner.setSelection(0)
            spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    getCourse((position+1).toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }

            // search view
            view.fragment_search.isSubmitButtonEnabled = true
            view.fragment_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    getCourse(query.toString())
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })

//        // Edit Text
//        view.fragment_button.setOnClickListener {
//            getCourse(view.fragment_courseNum.text.toString())
//        }

        }

        return view
    }

    private fun getCourse(courseNum: String) {
        CoroutineScope(Dispatchers.Main).launch {
            var list: ArrayList<CourseModel> = arrayListOf()

            framgent_loading.visibility = View.VISIBLE

            var response : String? = null
            withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                response = HttpTask.getJsonResponse(courseNum)
            }

            if (HttpTask.checkNoData(response)) {
                // json 응답을 받았으면 파싱
                // search 결과 한 번에 최하단 depth 까지 갈 수는 없단걸 알게됨.
                val dataArray: JsonArray = HttpTask.getDataArray(response)

                // 코스명 ui에 반영
                val courseName = dataArray[0].asJsonObject.get("courseName")
                fragment_courseName.text = courseName.asString

                // dataarray를 모두 돌아보면서 원하는 정보를 model에 넣고 list에 추가.
                list = HttpTask.getModelList(dataArray)

                framgent_loading.visibility = View.GONE

                mAdapter.setList(list)

            } else {
                Log.d("RESPONSE", "$response")
            }

        }
    }
}