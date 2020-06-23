package com.example.common.error

import java.lang.Exception

/**
 * ClassName:      NetWorkException
 * Description:    NetWorkException
 * Author:         mwy
 * CreateDate:     2020/6/23 21:42
 */
class NetWorkException (override var message: String = "NetWorkException", cause: Throwable?) : RuntimeException() {

}