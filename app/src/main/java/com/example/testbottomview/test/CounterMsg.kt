package com.example.testbottomview.test

import kotlinx.coroutines.CompletableDeferred

/**
 * ClassName:      CounterMsg
 * Description:    CounterMsg
 * Author:         mwy
 * CreateDate:     2020/8/26 19:03
 */
 sealed class CounterMsg
 object IncCounter: CounterMsg()//递增计数器的单向消息
 class GetCounter(val response: CompletableDeferred<Int>):CounterMsg()