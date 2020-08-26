package com.example.testbottomview.test

import kotlinx.coroutines.*
import org.junit.Test
import kotlin.system.measureTimeMillis

/**
 * ClassName:      CoroutineTest
 * Description:    CoroutineTest
 * Author:         mwy
 * CreateDate:     2020/8/25 16:37
 */
class CoroutineTest {

    @Test
    fun testRunningBlock() = runBlocking {
        launch {
            delay(200L)
            println("Task from runBlocking")
        }
        coroutineScope { // 创建⼀个协程作⽤域
            launch {
                delay(500L)
                println("Task from nested launch")
            }
            delay(100L)
            println("Task from coroutine scope") // 这⼀⾏会在内嵌 launch 之前输出
        }
        delay(1000L)//会阻塞主线程,类似于Tread的Sleep
        println("Coroutine scope is over") // 这⼀⾏在内嵌 launch 执⾏完毕后才输出
    }

    @Test
    fun testCancelCoroutine() = runBlocking() {
        val startTime = System.currentTimeMillis()
        val job = GlobalScope.launch {
            var nextPrintTime = startTime
            var i = 0
            //默认计算的协程 是不可取消的 只能等待执行完毕
            while (i < 5 && isActive) { // isActive 可以被取消的计算循环
                // 每秒打印消息两次
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("job: I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1300L) // 等待⼀段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并等待它结束
        println("main: Now I can quit.")
    }

    @Test
    fun testCancelFinally() = runBlocking {
        val job = launch {
            try {
                repeat(1000) { i ->
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                //如果在finally中运行挂起函数,抛出 JobCancellationException 因为这时协程已经被取消了
                /*try {
                    delay(1000L)
                    println("job: I'm running finally")
                }catch (e:Exception){
                    //需要主动打印出日志信息,之所以不会主动打印出错误日志信息,
                    //这是因为在被取消的协程中CancellationException 被认为是协程执⾏结束的正常原因
                    e.printStackTrace()
                }*/
                //如果要在finally中运行挂起函数 你可以将相应的代码包装在withContext(NonCancellable) {……} 中
                try {
                    withContext(NonCancellable) {
                        println("job: I'm running finally")
                        delay(1000L)
                        println("job: And I've just delayed for 1 sec because I'm non-cancellable")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        delay(1300L) // 延迟⼀段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并且等待它结束
        println("main: Now I can quit.")
    }

    @Test
    fun testWithTimeOut() = runBlocking {
        //设置超时
        //在这个⽰例中我们在 函数中正确地使⽤了 withTimeout 超时
        //会抛出异常 日志会打印TimeoutCancellationException: Timed out waiting for 2000 ms
        //因为withTimeout 被认为是非正常取消,所以会抛出异常 打印日志
        //如果要用withTimeout 而不希望程序崩溃 可以try catch TimeoutCancellationException
        withTimeout(2000L) {
            repeat(100) {
                println("I'm sleeping $it ...")
                delay(500)
            }
        }
    }

    @Test
    fun testWithTimeoutOrNull() = runBlocking {
        val result = withTimeoutOrNull(1300L) {
            try {
                repeat(1000) { i ->
                    println("I'm sleeping $i ...")
                    delay(500L)
                }
            } catch (e: TimeoutCancellationException) {
                //这里需要手动打印日志,因为这是被正常取消.否则控制台不会有日志
                // 在超时的情况下 result 的值为null 是正常取消了
                //所以不会crash,如果要看日志信息 则需要try catch 来打印日志
                e.printStackTrace()
            }
            "Done" // 在它运⾏得到结果之前取消它
        }
        println("Result is $result")
    }

    private suspend fun doSomethingUsefulOne(): Int {
        delay(1000L) // 假设我们在这⾥做了⼀些有⽤的事
        return 13
    }

    private suspend fun doSomethingUsefulTwo(): Int {
        delay(1000L) // 假设我们在这⾥也做了⼀些有⽤的事
        return 29
    }

    //顺序执行
    @Test
    fun testDefault() = runBlocking<Unit> {
        val time = measureTimeMillis {
            val one = doSomethingUsefulOne()
            val two = doSomethingUsefulTwo()
            println("The answer is ${one + two}")
        }
        println("Completed in $time ms")
    }

    //并发执行
    @Test
    fun testAsync() = runBlocking {
        var time = measureTimeMillis {
            val one = async { doSomethingUsefulOne() }//协程已经在执行了
            val two = async { doSomethingUsefulTwo() }//协程已经在执行了
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms")
    }

    //惰性的async
    //在这个模式下，
    //只有结果通过 await 获取的时候协程才会启动，或者在 Job 的 start 函数调⽤的时候
    //start函数可以由我们自己决定在什么时候执行
    //注意:如果我们只是在 println 中调⽤ await，⽽没有在单独的协程中调⽤ start，这将会导致顺序⾏
    //为，直到 await 启动该协程 执⾏并等待⾄它结束，这并不是惰性的预期⽤例
    @Test
    fun testLazyAsync() = runBlocking {
        var time = measureTimeMillis {
            val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
            val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
            one.start()//执行第一个
            two.start()//执行第二个
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms")
    }


    // somethingUsefulOneAsync 函数的返回值类型是 Deferred<Int>
    private fun somethingUsefulOneAsync() = GlobalScope.async {
        doSomethingUsefulOne()
    }

    // somethingUsefulTwoAsync 函数的返回值类型是 Deferred<Int>
    private fun somethingUsefulTwoAsync() = GlobalScope.async {
        doSomethingUsefulTwo()
    }

    //注意，这些 xxxAsync 函数不是 挂起 函数。它们可以在任何地⽅使⽤。然⽽，它们总是在调⽤它们的
    //代码中意味着异步（这⾥的意思是 并发 ）执⾏。
    //例如:
    @Test
    // 注意， 在这个⽰例中我们在 `testCoroutine` 函数的右边没有加上 `runBlocking`
    //
    fun testCoroutine() {
        val time = measureTimeMillis {
            // 我们可以在协程外⾯启动异步执⾏
            val one = somethingUsefulOneAsync()
            val two = somethingUsefulTwoAsync()
            // 但是等待结果必须调⽤其它的挂起或者阻塞
            // 当我们等待结果的时候， 这⾥我们使⽤ `runBlocking { …… }` 来阻塞主线程
            runBlocking {
                println("The answer is ${one.await() + two.await()}")
            }
        }
        println("Completed in $time ms")
    }
    //这种带有异步函数的编程⻛格仅供参考，因为这在其它编程语⾔中是⼀种受欢迎的⻛格。在 Kotlin
    //的协程中使⽤这种⻛格是强烈不推荐的，原因如下所述。
    //考虑⼀下如果 val one = somethingUsefulOneAsync() 这⼀⾏和 one.await() 表达式这⾥
    //在代码中有逻辑错误，并且程序抛出了异常以及程序在操作的过程中中⽌，将会发⽣什么。通常情况
    //下，⼀个全局的异常处理者会捕获这个异常，将异常打印成⽇记并报告给开发者，但是反之该程序将会
    //继续执⾏其它操作。但是这⾥我们的 somethingUsefulOneAsync 仍然在后台执⾏，尽管如此，启
    //动它的那次操作也会被终⽌。这个程序将不会进⾏结构化并发

    //--------------------------------------------------------------------------------------

    //推荐使⽤ async 的结构化并发:
    private suspend fun concurrentSum(): Int = coroutineScope {
        val one = async { doSomethingUsefulOne() }
        val two = async { doSomethingUsefulTwo() }
        one.await() + two.await()
    }

    @Test
    //这种情况下，如果在 concurrentSum 函数内部发⽣了错误，并且它抛出了⼀个异常，所有在作⽤域
    //中启动的协程都会被取消
    fun testConcurrentSum() = runBlocking {
        val time = measureTimeMillis {
            println("The answer is ${concurrentSum()}")
        }
        println("Completed in $time ms")
    }

    //出错误的情况
    private suspend fun failedConcurrentSum(): Int = coroutineScope {
        val one = async<Int> {
            try {
                delay(Long.MAX_VALUE) // 模拟⼀个⻓时间的运算
                42
            } finally {
                println("First child was cancelled")
            }
        }
        val two = async<Int> {
            println("Second child throws an exception")
            throw ArithmeticException()
        }
        one.await() + two.await()
    }

    @Test
    //如果其中⼀个⼦协程（即 two ）失败，第⼀个 async 以及等待中的⽗协程都会被取消
    fun testCancelInScope() = runBlocking<Unit> {
        try {
            val result = failedConcurrentSum()
            println("result: $result")
        } catch (e: ArithmeticException) {
            println("Computation failed with ArithmeticException")
        }
    }

    //---------------------------------协程上下文----------------------------------------

    @ObsoleteCoroutinesApi
    @Test
    fun testCoroutineContext() = runBlocking<Unit> {
        launch { // 运⾏在⽗协程的上下⽂中， 即 runBlocking 主协程
            println("main runBlocking : I'm working in thread${Thread.currentThread().name}")
        }
        launch(Dispatchers.Unconfined) { // 不受限的——将⼯作在主线程中
            println("Unconfined : I'm working in thread${Thread.currentThread().name}")
        }
        launch(Dispatchers.Default) { // 将会获取默认调度器
            println("Default : I'm working in thread ${Thread.currentThread().name}")
        }
        launch(Dispatchers.IO) {
            println("IO : I'm working in thread ${Thread.currentThread().name}")
        }
        /*launch(Dispatchers.Main) {
            println("Main : I'm working in thread ${Thread.currentThread().name}")
        }*/
        launch(newSingleThreadContext("MyOwnThread")) { // 将使它获得⼀个新的线程
            println("newSingleThreadContext: I'm working in thread${Thread.currentThread().name}")
        }
    }

    //查看上下文
    @Test
    fun testCheckCoroutineContext() = runBlocking<Unit> {
        println("My job is ${coroutineContext[Job]}")
    }


    //当⼀个协程被其它协程在 CoroutineScope 中启动的时候，它将通过
    //CoroutineScope.coroutineContext 来承袭上下⽂，并且这个新协程的 Job 将会成为⽗协程作业的 ⼦
    //作业。当⼀个⽗协程被取消的时候，所有它的⼦协程也会被递归的取消。
    //然⽽，当使⽤ GlobalScope 来启动⼀个协程时，则新协程的作业没有⽗作业。因此它与这个启动的作⽤
    //域⽆关且独⽴运作
    @Test
    fun testCancelGlobalScope() = runBlocking{
        // 启动⼀个协程来处理某种传⼊请求（request）
        val request = launch {
            // 孵化了两个⼦作业, 其中⼀个通过 GlobalScope 启动
            GlobalScope.launch {
                println("job1: 我在GlobalScope中运行并独立执行！")
                delay(1000)
                println("job1: 我不受取消请求的影响")
            }
            // 另⼀个则承袭了⽗协程的上下⽂
            launch {
                delay(100)
                println("job2: 我是子协程")
                delay(1000)
                println("job2: 如果我的父请求被取消，我将不执行此行")
            }
        }
        delay(500)
        request.cancel() // 取消请求（request） 的执⾏
        delay(1000) // 延迟⼀秒钟来看看发⽣了什么
        println("main: 谁在取消请求后幸存下来？")
    }

    @Test
    fun testChildCoroutine() = runBlocking{
        val request = launch {
            //循环启动子协程
            repeat(3){
                launch {
                    delay((it + 1) * 200L)// 延迟 200 毫秒、 400 毫秒、 600 毫秒的时间
                    println("Coroutine $it is done")
                }
            }
            println("request: I'm done and I don't explicitly join my children that are still active")
        }
        request.join()// 等待请求的完成， 包括其所有⼦协程
        println("Now processing of the request is complete")
    }

    //给协程命名
    @Test
    fun testCoroutineName() = runBlocking<Unit> {
        val one = async(CoroutineName("协程1")){
            delay(500L)
            println("CoroutineName:${Thread.currentThread().name}")
        }
        launch(Dispatchers.Default + CoroutineName("协程2")) {
            println("CoroutineName: ${Thread.currentThread().name}")
        }
    }

    //ThreadLocal，asContextElement
    @Test
    fun testAsContextElement() = runBlocking<Unit> {
        val threadLocal = ThreadLocal<String?>()
        threadLocal.set("main")
        println("Pre-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        val job = launch(Dispatchers.Default + threadLocal.asContextElement(value = "launch")) {
            println("Launch start, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
            yield()
            println("After yield, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        }
        job.join()
        println("Post-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
    }


}