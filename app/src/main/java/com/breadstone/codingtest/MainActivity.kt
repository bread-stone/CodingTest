package com.breadstone.codingtest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = "CodingTest"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Log.d(TAG, "${stringToIntSum(10)}")
//        Log.d(TAG, "${stringToIntSum(12)}")
//        Log.d(TAG, "${stringToIntSum(13)}")
//        Log.d(TAG, "${stringToIntSum(14)}")
//        var id_list = arrayOf<String>("muzi", "frodo", "apeach", "neo")
//        var report = arrayOf<String>("muzi frodo","apeach frodo","frodo neo","muzi neo","apeach muzi")
//        val k = 2

//        var id_list = arrayOf<String>("con", "ryan")
//        var report = arrayOf<String>("ryan con", "ryan con", "ryan con", "ryan con")
//        val k = 3
//        val ret = checkVanUser(id_list, report, k)
//        Log.d(TAG, "${ret.toString()}")

//        val n = 997244
//        val k = 3
//        val n = 12
//        val k = 3
//        Log.d(TAG,"result : ${checkPrime(n, k)}")

//        val fees = intArrayOf(180, 5000, 10, 600)
//        val records = arrayOf(
//            "05:34 5961 IN",
//            "06:00 0000 IN",
//            "06:34 0000 OUT",
//            "07:59 5961 OUT",
//            "07:59 0148 IN",
//            "18:59 0000 IN",
//            "19:09 0148 OUT",
//            "22:59 5961 IN",
//            "23:00 5961 OUT"
//        )

        val fees = intArrayOf(120, 0, 60, 591)
        val records = arrayOf("16:00 3961 IN","16:00 0202 IN","18:00 3961 OUT","18:00 0202 OUT","23:58 3961 IN")
//
//        val fees = intArrayOf(1, 461, 1, 10)
//        val records = arrayOf("00:00 1234 IN")
        Log.d(TAG, "parkingFee : ${parkingFee(fees, records).toString()}")
    }

    fun parkingFee(fees: IntArray, records: Array<String>): IntArray {
        val carFee = LinkedHashMap<String, Int>()
        val carTime = LinkedHashMap<String, Int>()
        val carCheckIn = LinkedHashMap<String, Int>()

        records.forEach { r ->
            r.split(" ").run {
                val timeInt = this[0].split(":").run { this[0].toInt() * 60 + this[1].toInt() }
                val carNum = this[1]
                val inOut = this[2]
                if (this[2] == "IN") {
                    carCheckIn[this[1]] = timeInt
                } else {
                    carTime[this[1]]?.let {
                        carTime[this[1]] = it + timeInt - carCheckIn[this[1]]!!
                    } ?: carTime.put(this[1],  timeInt - carCheckIn[this[1]]!!)
                    carCheckIn[this[1]] = -1
                }
            }
        }

        carCheckIn.filter { it.value != -1 }.forEach {
            carTime[it.key]?.let { time ->
                carTime[it.key] = time + 23 * 60 + 59 - it.value
            } ?: carTime.put(it.key, 23 * 60 + 59 - it.value)
        }

        carTime.forEach {
            val fee = calcFee(fees, it.value)
            Log.d(TAG, "fee : $fee")
            carFee.put(it.key, fee)
        }

        return carFee.toSortedMap().values.toIntArray()
    }

    private fun calcFee(fees: IntArray, total:Int): Int {
        val defaultTime = fees[0]
        val defaultFee = fees[1]
        val unitTime = fees[2]
        val unitFee = fees[3]
        return if((total - defaultTime) <= 0) defaultFee
        else (defaultFee + Math.ceil(((total - defaultTime) / unitTime.toDouble())) * unitFee).toInt()
    }

    fun checkPrime(n: Int, k: Int): Int {
        if (n < 1 || n > 1000000) return 0
        if (k < 3 || k > 10) return 0
        var ret = 0
        n.toString(k).run {
            ret = split("0")
                .filter { str ->
                    str.isNotEmpty() && isPrime(str.toLong())
                }.size
        }
        return ret
    }

    fun isPrime(n: Long): Boolean {
        if (n <= 1L) return false
        if (n == 2L || n == 3L) return true

        for (i in 2..Math.sqrt(n.toDouble()).toLong()) {
            if (n % i == 0L) {
                return false
            }
        }
        return true
    }

    //제한시간 10초
    fun checkVanUser(id_list: Array<String>, report: Array<String>, k: Int): IntArray {
        var vanUser = hashMapOf<String, ArrayList<String>>() //신고당한사람, 신고한사람
        var reportUser = LinkedHashMap<String, Int>() //신고한 사람

        id_list.forEach { user_id ->
            reportUser[user_id] = 0
        }

        report.forEach { r ->
            val infos = r.split(" ")
            vanUser[infos[1]]?.let {
                it.add(infos[0])
            } ?: vanUser.put(infos[1], arrayListOf<String>(infos[0]))
        }

        vanUser.forEach { van ->
            van.value.distinct().also {
                if (it.size >= k) {
                    it.forEach { reporter ->
                        reportUser.get(reporter)?.let {
                            reportUser.put(reporter, it + 1)
                        }
                    }
                }
            }
        }
        return reportUser.values.toList().toIntArray()
    }


    fun stringToIntSum(x: Int): Boolean {
        var answer = false
        if (x < 1 || x > 10000) return answer

        var sumResult = 0
        x.toString().forEach {
            sumResult += (it.code - 48)
        }
        answer = (x.rem(sumResult) == 0)
        return answer
    }
}