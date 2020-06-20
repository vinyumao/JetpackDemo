package com.example.common

import kotlinx.coroutines.CoroutineScope

internal typealias Block = suspend CoroutineScope.() -> Unit