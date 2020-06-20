package com.example.common

import java.lang.Exception

/**
 * ClassName:      CoroutineState
 * Description:    CoroutineState
 * Author:         mwy
 * CreateDate:     2020/6/20 20:04
 */
class Request(var state:State,var showDialog:Boolean = false, var exception: Exception? = null) {
    enum class State {
        START,
        REFRESH,
        FINISH,
        ERROR
    }
}