package com.example.jazzbargame.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jazzbargame.DEFINES as D
import com.example.jazzbargame.R
import com.example.jazzbargame.adapter.CustomerAdapter
import com.example.jazzbargame.model.CustomerModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    // Recycler adapter
    private lateinit var mAdapter: CustomerAdapter

    // list
    private var customerList: ArrayList<CustomerModel> = arrayListOf()
    private val imageButtonList: ArrayList<ImageButton> = arrayListOf()
    private val clickedList: ArrayList<Boolean> = arrayListOf()

    // progress bar
    private lateinit var progress: ProgressBar
    private val goal: Int = 3500
    private var curr: Int = 0

    // option
    private var alreadyPosition :Int = -1

    override fun onPause() {
        super.onPause()
        onResume()
    }

    override fun onDestroy() {
        Log.d("LOG", "Jazz Bar Game's MainAcitivity is dead.")
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 고객 정보를 불러들여옴.
        // 나중에 DB를 생성하면 수정해야 함.
        setAdapterList()
        loadCustomer()

        // music service
        Toast.makeText(this, "연주자를 클릭하여 재즈바가 열렸음을 알리세요.", Toast.LENGTH_LONG).show()
        findViewById<ImageButton>(R.id.main_button_hole).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startService(intent)
        }

        // progress bar
        progress = findViewById(R.id.main_profit)
        progress.max = goal
        progress.progress = 1
        findViewById<TextView>(R.id.main_goal).text = "Goal Money : " + goal.toString()

        // image button -> 7 items
        imageButtonList.add(findViewById(R.id.main_button_1))
        imageButtonList.add(findViewById(R.id.main_button_2))
        imageButtonList.add(findViewById(R.id.main_button_3))
        imageButtonList.add(findViewById(R.id.main_button_4))
        imageButtonList.add(findViewById(R.id.main_button_5))
        for (i in 0 until imageButtonList.size) {
            clickedList.add(false)
        }
        Log.d("LOG", "onCreate is done")
    }


    override fun onResume() {
        super.onResume()

        // recycler click listener
        mAdapter.setItemClickListener(object : CustomerAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                // get customer, position, progress current
                Log.d("LOG", "position : $position")
                val customer: CustomerModel = customerList[position]

                // progress modified
                curr += customer.money.toInt()
                main_profit.setProgress(curr)
                Log.d("LOG", "curr : ${main_profit.progress}")
                // text view modified
                findViewById<TextView>(R.id.main_goal).text = "Goal Profit : $curr / $goal"

                // list modified
                customerList.removeAt(position)
                mAdapter.setList(customerList)
                Log.d("LOG", "customerList is changed")

                // cancel button
                findViewById<ImageButton>(R.id.main_button_cancel).setOnClickListener {
                    if (customerList[customerList.size-1].id != customer.id) {
                        customerList.add(customer)
                        mAdapter.setList(customerList)
                        Toast.makeText(this@MainActivity, "선택한 손님이 다시 기다립니다.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@MainActivity, "선택한 손님이 없습니다.", Toast.LENGTH_LONG).show()
                    }
                }

                // full seat toast
                if (false !in clickedList) {
                    Toast.makeText(
                        this@MainActivity,
                        "더 이상 앉을 자리가 없습니다. 마음에 안 드는 놈을 쳐내십시오.",
                        Toast.LENGTH_LONG
                    ).show()
                }

                // imageButton click Listener
                thread(start = true) {
                    for (i in 0 until imageButtonList.size) {
                        imageButtonList[i].setOnClickListener {
                            // click 된 적이 있다면 원상복구
                            if (clickedList[i] == true) {
                                imageButtonList[i].setImageResource(R.drawable.image_chair)
                                clickedList[i] = false
                            } else {
                                imageButtonList[i].setImageResource(customer.imageProfile)
                                clickedList[i] = true
                                // once chair button clicked, progress is increased.
                                runOnUiThread {
                                    progress.setProgress(curr)
                                }
                            }
                        }
                    }
                }

                // exit
                if (goal <= curr) {
                    Toast.makeText(this@MainActivity, "오늘 목표치에 달성했습니다. 문 닫아도 됩니다.", Toast.LENGTH_LONG).show()
                    finish()
                }
                // additional customer
                setAdapterList()
            }

        })

    }

    private fun setAdapterList() {
        // DB reading
        if (customerList.size == 0) {
            Log.d("LOG", "customer list left one")
            Toast.makeText(this@MainActivity, "새로운 손님들이 찾아왔습니다.", Toast.LENGTH_LONG).show()
            // 여기서 DB 쪽에 있는 놈들을 불러와야 하는 것이다리.
            customerList = D.entityToModel()
        }

    }

    // Adapter : Activity(list) -> Adapter
    private fun loadCustomer() {
        mAdapter = CustomerAdapter()

        // customer를 list에 표현.
        mAdapter.setList(customerList)
        val recyclerList = findViewById<RecyclerView>(R.id.main_list)
        recyclerList.layoutManager = LinearLayoutManager(this)
        recyclerList.adapter = mAdapter
    }


}