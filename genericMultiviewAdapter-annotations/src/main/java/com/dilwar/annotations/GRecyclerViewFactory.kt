package com.dilwar.annotations

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
 annotation class GRecyclerViewFactory(
    val classes: Array<KClass<*>>,
    val enableDataBinding: Boolean = false
)
