package com.example.common.error

import java.lang.Exception
import java.lang.RuntimeException

/**
 * ClassName:      ApiException
 * Description:    ApiException
 * Author:         mwy
 * CreateDate:     2020/6/23 21:40
 */
class ApiException(override var message: String = "ApiException") : RuntimeException() {

}