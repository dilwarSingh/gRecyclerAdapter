package com.dilwar.annotations

import android.support.annotation.LayoutRes

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class GRecyclerViewType(@LayoutRes val layout: Int)