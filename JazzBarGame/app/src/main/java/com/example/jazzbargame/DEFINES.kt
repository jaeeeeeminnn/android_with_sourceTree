package com.example.jazzbargame

import com.example.jazzbargame.Database.CustomerDatabase
import com.example.jazzbargame.entity.Customer
import com.example.jazzbargame.model.CustomerModel

object DEFINES {

    // DB는 splash, Main 모두에서 사용해야 함.
    // Splash에서 DB에 뭔가를 넣고,
    // Main에서 SELECT 나 UPDATE 아니면 GETALL 같은 걸 해야 함.
    var customerDatabase : CustomerDatabase? = null

    // DB's Customer + Adapter's CustomerModel
    // id is used only for DB
    var id = 0

    // int range for Random Selection
    val range_01 = (0..1)
    val range_name = (0..10)
    val range_money = (100..1000)

    // entityToModel return list
    private var modelList : ArrayList<CustomerModel> = arrayListOf()

    // Candidates of Name
    val NAME_CANDI : ArrayList<String> = arrayListOf(
            "Glenn Miller", "Duke Elington", "Cutie Williams",
            "Sellonius Monk", "Jaco Pastorius", "Louis Armstrong",
            "Sam Hamington", "Jac Willy", "Abriahn Lincorn", "Jake Sully"
    )

    // Candidates of Gender
    val GENDER_CANDI : ArrayList<String> = arrayListOf("Male", "Female")

    // Candidates of Image Resource
    val IMAGE_CANDI : ArrayList<Int> = arrayListOf(
        R.drawable.ic_baseline_person_24,
        R.drawable.ic_baseline_perm_identity_24
    )

    // DB의 Entity list에서 Adapter의 Model list로 변환하기 위한 함수.
    // recycler list에 item이 모자랄 때 채워주는 역할을 할 거임.
    fun entityToModel() : ArrayList<CustomerModel> {
        val tempList : List<Customer> = customerDatabase!!.CustomerDao().getAll()
        for (i in 0 until tempList.size) {
            modelList.add(
                CustomerModel(
                    tempList[i].id,
                    tempList[i].imageProfile,
                    tempList[i].name,
                    tempList[i].gender,
                    tempList[i].money.toString()
                )
            )
        }
        return modelList
    }

    fun loadDatabase() {
        if (customerDatabase!!.CustomerDao().getCount() < 10) {
            for (i in 0 until 10) {
                customerDatabase!!.CustomerDao().insert(
                    Customer(
                        id,
                        IMAGE_CANDI[range_01.random()],
                        NAME_CANDI[range_name.random()],
                        GENDER_CANDI[range_01.random()],
                        range_money.random()
                    )
                )
                id++
            }
        }
    }

}