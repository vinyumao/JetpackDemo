package com.example.testbottomview.test

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.junit.Test
import kotlin.system.measureTimeMillis

/**
 * ClassName:      CoroutineTest2
 * Description:    CoroutineTest2
 * Author:         mwy
 * CreateDate:     2020/8/26 13:43
 */
class CoroutineTest2 {

    private fun fooFlow(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100)
            emit(i)
        }
    }

    @Test
    fun testFlow() = runBlocking<Unit> {
        launch {
            for (i in 1..3) {
                println("I'm not blocked $i")
                delay(100)
            }
        }

        fooFlow().collect { println(it) }
    }

    private fun fooFlow2(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100)
            println("emitting $i")
            emit(i)
        }
    }

    @Test
    fun testCancelFlow() = runBlocking<Unit> {
        withTimeoutOrNull(250) {
            fooFlow2().collect { println(it) }
        }
        for (i in 1..10) {
            delay(100)
            println("for $i")
        }
        println("Done")
    }

    @Test
    fun testCancelFlow2() = runBlocking<Unit> {
        withTimeoutOrNull(250) {
            repeat(10) {
                delay(100)
                println("for $it")
            }
        }
        //println("Done")
    }

    @Test
    fun testCancelFlow3() = runBlocking<Unit> {
        println("runBlocking:My job is ${coroutineContext[Job]}")
        withTimeoutOrNull(250) {
            CoroutineScope(Dispatchers.Default).launch {
                println("Default:My job is ${coroutineContext[Job]}")

                fooFlow2().collect {
                    println("fooFlow2:My job is ${coroutineContext[Job]}")
                    println(it)
                }
            }
        }
        //println("Done")
    }

    @Test
    fun cancelWithTimeOut() = runBlocking<Unit> {
        val result = withTimeoutOrNull(250) {
            try {
                repeat(100) {
                    delay(100)
                    println("for $it")
                }
                "Done"
            } catch (e: TimeoutCancellationException) {
                e.printStackTrace()
            }
        }

    }

    @Test
    fun cancelWithTimeOut2() = runBlocking<Unit> {
        val result = withTimeoutOrNull(250) {
            try {
                CoroutineScope(Dispatchers.Default).launch {
                    println("Default:My job is ${coroutineContext[Job]}")
                    fooFlow2().collect {
                        println("fooFlow2:My job is ${coroutineContext[Job]}")
                        println(it)
                    }
                }
                "Done"
            } catch (e: TimeoutCancellationException) {
                e.printStackTrace()
            }
        }
    }

    @Test
    fun testFooFlow() = runBlocking<Unit> {
        CoroutineScope(Dispatchers.IO).launch {
            println("Default:My job is ${coroutineContext[Job]}")
            fooFlow2().collect {
                //这里是运行不到还是怎么样,这里的代码运行不到
                //但是放app里面是可以的,Test测试就不行 不知道为啥..
                //或许是CoroutineContext的原因,在不同环境的原因
                println("fooFlow2:My job is ${coroutineContext[Job]}")
                println(it)
            }
        }
    }

    @Test
    fun testAsFlow() = runBlocking {
        val list = listOf<Int>(1, 2, 3)
        list.asFlow().collect { println(it) }
        (1..3).asFlow().collect { println(it) }
        //这打印的是啥玩意.. 直接打印出了区间1..3
        flowOf(1..3).collect { println(it) }
        flowOf(1, 2, 3).collect { println(it) }
    }


    private suspend fun performRequest(request: Int): String {
        delay(1000)
        return "response : $request"
    }

    @Test
    fun testMap() = runBlocking<Unit> {
        (1..3).asFlow()
            .map { performRequest(it) }
            .collect { println(it) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testTransform() = runBlocking<Unit> {
        (1..3).asFlow()
            .transform { emit(performRequest(it)) }
            .collect { println(it) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testTake() = runBlocking {
        (1..3).asFlow()
            .take(2)
            .collect { println(it) }
    }

    //末端操作符
    //末端操作符是在流上⽤于启动流收集的挂起函数。collect 是最基础的末端操作符，但是还有另外⼀些
    //更⽅便使⽤的末端操作符：
    //转化为各种集合，例如 toList 与 toSet。
    //获取第⼀个（first）值与确保流发射单个（single）值的操作符。
    //使⽤ reduce 与 fold 将流规约到单个值。

    @ExperimentalCoroutinesApi
    @Test
    fun testEndOperator() = runBlocking {
        val sum = (1..5).asFlow()
            .map { it * it }
            .reduce { accumulator, value -> accumulator + value }
        println(sum)
    }

    private fun fooFlow3() = flow {
        //flow {...} 构建器中的代码必须遵循上下⽂保存属性，并且不允许从其他上下⽂中发射（emit）
        kotlinx.coroutines.withContext(Dispatchers.Default) {
            for (i in 1..3) {
                Thread.sleep(1000)
                println("Emitting $i")
                emit(i)
            }
        }
    }

    //如果要更改流发射的上下⽂,可以用flowOn
    @ExperimentalCoroutinesApi
    private fun fooFlow4() = flow {
        for (i in 1..3) {
            Thread.sleep(1000)
            println("Emitting $i")
            emit(i)
        }
    }.flowOn(Dispatchers.Default)

    @ExperimentalCoroutinesApi
    @Test
    fun testFlow2() = runBlocking<Unit> {
        //运行报错
        //fooFlow3().collect { println(it) }
        fooFlow4().collect { println(it) }
    }

    //flow是顺序执行的,效率较慢
    private fun fooFlow5(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100) // 假装我们异步等待了 100 毫秒
            emit(i) // 发射下⼀个值
        }
    }

    @Test
    fun testFlow3() = runBlocking {
        val time = measureTimeMillis {
            fooFlow5().collect {
                delay(300)// 假装我们花费 300 毫秒来处理它
                println(it)
            }
        }
        //由于是顺序执行的 每次要花费400毫秒左右,总共花费1200毫秒左右
        println("Collected in $time ms")
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testBuffFlow() = runBlocking {
        val time = measureTimeMillis {
            fooFlow5().buffer()
                .collect {
                    delay(300)// 假装我们花费 300 毫秒来处理它
                    println(it)
                }
        }
        //由于是并发的 所以只是在等待的时候花费了900毫秒 加执行的是100毫秒 总共花费1000多毫秒
        println("Collected in $time ms")
        //注意，当必须更改 CoroutineDispatcher 时，flowOn 操作符使⽤了相同的缓冲机制，但是我们在
        //这⾥显式地请求缓冲⽽不改变执⾏上下⽂
    }

    //合并 conflate
    //当流代表部分操作结果或操作状态更新时，可能没有必要处理每个值，⽽是只处理最新的那个。在本⽰
    //例中，当收集器处理它们太慢的时候，conflate 操作符可以⽤于跳过中间值。
    @ExperimentalCoroutinesApi
    @Test
    fun testConflateFlow() = runBlocking {
        val time = measureTimeMillis {
            fooFlow5().conflate()
                .collect {
                    delay(300)// 假装我们花费 300 毫秒来处理它
                    println(it)
                }
        }
        println("Collected in $time ms")
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testCollectLatestFlow() = runBlocking {
        val time = measureTimeMillis {
            fooFlow5()
                //当发射器和收集器都很慢的时候，合并是加快处理速度的⼀种⽅式。它通过删除发射值来实现。另⼀种
                //⽅式是取消缓慢的收集器，并在每次发射新值的时候重新启动它
                .collectLatest { // 取消并重新发射最后⼀个值
                    println("Collecting $it")
                    delay(300) // 假装我们花费 300 毫秒来处理它
                    println("Done $it")
                }
        }
        //发射3次耗费300多毫秒 但收集时值耗费一次300多毫秒,总共花费600多毫秒左右
        println("Collected in $time ms")
    }

    //组合多个流 zip
    @ExperimentalCoroutinesApi
    @Test
    fun testZip() = runBlocking {
        val num = (1..4).asFlow()
        val str = flowOf("one", "two", "three")
        num.zip(str) { a, b ->
            "$a -> $b"
        }.collect {
            println(it)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testZip2() = runBlocking {
        val num = (1..3).asFlow().onEach { delay(300) }//每300毫秒发送一个数字
        val str = flowOf("one", "two", "three")
            .onEach { delay(400) }//每400毫秒发送一个字符串
        val startTime = System.currentTimeMillis() // 记录开始的时间
        val time = measureTimeMillis {
            num.zip(str) { a, b -> "$a -> $b" }
                .collect { println("$it at ${System.currentTimeMillis() - startTime} ms from start")}
        }
        //会花费1200多毫秒左右
        println("Done in $time ms")
    }

    //⽤ combine
    //当流表⽰⼀个变量或操作的最新值时，可能需要执⾏计算，这依赖于相应
    //流的最新值，并且每当上游流产⽣值的时候都需要重新计算。
    @ExperimentalCoroutinesApi
    @Test
    fun testCombine() = runBlocking {
        val num = (1..3).asFlow().onEach { delay(300) }//每300毫秒发送一个数字
        val str = flowOf("one", "two", "three")
            .onEach { delay(400) }//每400毫秒发送一个字符串
        val startTime = System.currentTimeMillis() // 记录开始的时间
        val time = measureTimeMillis {
            //nums 或 strs 流中的每次发射都会打印⼀⾏
            num.combine(str) { a, b -> "$a -> $b" }
                .collect { println("$it at ${System.currentTimeMillis() - startTime} ms from start") }
        }
        println("Done in $time ms")
    }

    //展平流
    private fun requestFlow(i: Int): Flow<String> = flow {
        emit("$i: First")
        delay(500) // 等待 500 毫秒
        emit("$i: Second")
    }

    @FlowPreview
    @Test
    //(1..3).asFlow().map { requestFlow(it) }
    //然后我们得到了⼀个包含流的流（ Flow<Flow<String>> ），需要将其进⾏展平为单个流以进⾏下⼀
    //步处理
    fun testFlatMapConcat() = runBlocking<Unit> {
        val startTime = System.currentTimeMillis() // remember the start time
        (1..3).asFlow().onEach { delay(100) } // a number every 100 ms
            .flatMapConcat { requestFlow(it) }
            .collect { value -> // collect and print
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }
    }

    //flatMapMerge
    @FlowPreview
    @Test
    fun testFlatMapMerge() = runBlocking<Unit> {
        val startTime = System.currentTimeMillis() // remember the start time
        (1..3).asFlow().onEach { delay(100) } // a number every 100 ms
            .flatMapMerge { requestFlow(it) }
            .collect { value -> // collect and print
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }
    }
    //flatMapLatest
    @ExperimentalCoroutinesApi
    @Test
    fun testFlatMapLatest() = runBlocking<Unit> {
        val startTime = System.currentTimeMillis() // remember the start time
        (1..3).asFlow().onEach { delay(100) } // a number every 100 ms
            .flatMapLatest { requestFlow(it) }
            .collect { value -> // collect and print
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }
    }

    //--------------------------并发--------------------------
    private suspend fun massiveRun(action: suspend () -> Unit) {
        val n = 100 // 启动的协程数量
        val k = 1000 // 每个协程重复执⾏同⼀动作的次数
        val time = measureTimeMillis {
            coroutineScope { // 协程的作⽤域
                repeat(n) {
                    launch {
                        repeat(k) { action() }
                    }
                }
            }
        }
        println("Completed ${n * k} actions in $time ms")
    }

    @ObsoleteCoroutinesApi
    @Test
    //以粗粒度限制线程
    fun testNewSingleThreadContext() = runBlocking {
        var counter = 0
        val context = newSingleThreadContext("counterContext")
        withContext(context){
            massiveRun {
                counter++
            }
        }
        println("Counter = $counter")
    }

    @Test
    //Mutex 相当于java中的synchronized 或者 ReentrantLock功能 以细粒度限制线程
    // withLock 扩展函数，可以⽅便的替代常⽤的 mutex.lock(); try { …… } finally {
    //mutex.unlock() } 模式
    fun testMutex() = runBlocking {
        var counter = 0
        val mutex = Mutex()
        withContext(Dispatchers.Default){
            massiveRun {
                mutex.withLock {
                    counter++
                }
            }
        }
        println("Counter = $counter")
    }

    //Actors

    @ObsoleteCoroutinesApi
    private fun CoroutineScope.counterActor() = actor<CounterMsg> {
        var counter = 0;//actor状态
        for(msg in channel){// 即将到来消息的迭代器
            when(msg){
                is IncCounter -> counter++
                is GetCounter -> msg.response.complete(counter)
            }
        }
    }

    @Test
    fun testActor() = runBlocking<Unit> {
        val counter = counterActor()
        withContext(Dispatchers.Default){
            massiveRun {
                counter.send(IncCounter)
            }
        }
        // 发送⼀条消息以⽤来从⼀个 actor 中获取计数值
        val response = CompletableDeferred<Int>()
        counter.send(GetCounter(response))
        println("Counter = ${response.await()}")
        counter.close()//关闭actor
    }
}