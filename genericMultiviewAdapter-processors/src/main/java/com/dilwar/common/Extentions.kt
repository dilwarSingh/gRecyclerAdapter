package com.dilwar.common

import java.util.*

fun String.firstCharSmall(): String {
    if (isEmpty()) return this
    return substring(0, 1).toLowerCase(Locale.ROOT).toCharArray()[0] + substring(1, length)
}