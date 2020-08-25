package com.example.testbottomview.test

import com.example.base.L

/**
 * ClassName:      InlineTest
 * Description:    Inline函数,类 测试
 * Author:         mwy
 * CreateDate:     2020/8/21 15:42
 */
inline class UserName(val userName: String)
inline class PassWord(val passWord: String)
class InlineTest {
    private fun login(userName: UserName, passWord: PassWord) {
        L.i("login...")
    }

    fun testLogin() {
        val userName = UserName("root")//UserName jvm编译后会替换成String
        val passWord = PassWord("123")
        //编译错误,顺序位置放反了,虽然UserName,PassWord 虽然最终会被编译器 编译为基本类型String
        //如果不用inline ,login函数参数为String 你参数位置防反了,并不会报错.但是登录逻辑错误,导致登录不上
        //这种参数放反的bug难找. 但是如果用inline 则可以在编译阶段 idea就会提示错误
        //login(passWord,userName)
        login(userName, passWord)
    }

    private fun calculation(a: Int, b: Int, block: (Int, Int) -> Int): Int = block(a, b)

    private val sum: (Int, Int) -> Int = { a, b -> a + b }

    fun testCalculation() {
        var result = calculation(1, 2, sum)
        var result2 = calculation(2, 3, sum)
        L.i("$result,$result2")
    }

    private inline fun test1(block1: () -> Unit) {
        block1.invoke()
        L.i("test1 inner")
        return
    }

    private inline fun test2(crossinline block2: () -> Unit) {
        block2.invoke()
        L.i("test2 inner")
        return
    }

    //noinline 修饰的block4 用来表示表示block4不是内联函数
    private inline fun test3(block3: () -> Unit, noinline block4: () -> Unit): () -> Unit {
        block3.invoke()
        block4.invoke()
        L.i("test3 inner")
        return block4
    }

    fun testInlineFunction() {
        test1 {
            L.i("test1 before")
            //return//直接返回 后面所有代码均不执行 其实这之后的代码在class中根本就没有
            L.i("test1 after")
        }
        test2 {
            L.i("test2 before")
            return@test2//crossinline修饰的 return不会中断代码执行 只是在本lambda函数终止 test2 after不会打印,但后面的test3还是会继续执行
            L.i("test2 after")
        }
        test3({
            L.i("test3 before")
            return
            L.i("test3 after")
        }, {
            L.i("test3 function2")
        })
    }
}