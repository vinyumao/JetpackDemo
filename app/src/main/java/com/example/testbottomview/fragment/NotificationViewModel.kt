package com.example.testbottomview.fragment

import androidx.lifecycle.ViewModel
import com.example.base.L

class NotificationViewModel : ViewModel() {

    fun testTailrec() {
        var result = addTo10(5)
        L.i("tailrec result:$result")
    }

    //尾递归
    //通常⽤循环写的算法改⽤递归函数来写，⽽⽆堆栈溢出的⻛险。
    private tailrec fun addTo10(x: Int = 1): Int {
        return if (x >= 10) x else {
            val y = x + 1
            L.i("tailrec y:$y")
            addTo10(y)
        }
    }
}