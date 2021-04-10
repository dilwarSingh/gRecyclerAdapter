package com.dilwar.common

import com.squareup.kotlinpoet.ClassName
import javax.lang.model.type.TypeMirror

object Constants {
    val classIntent = ClassName("android.content", "Intent")
    val classContext = ClassName("android.content", "Context")
    val classView = ClassName("android.view", "View")
    val VIEW_TYPE_SUFFIX = "GViewType"
    val VIEW_FACTORY_SUFFIX = "GViewFactory"
    val classRecyclerView_ViewHolder =
        ClassName("androidx.recyclerview.widget.RecyclerView", "ViewHolder")
    val classGViewType = ClassName("com.dilwar.multiViewAdapter", "GViewType")
    val classBaseRAdapter = ClassName("com.dilwar.multiViewAdapter", "BaseMultiViewAdapter")
    val classGViewFactory = ClassName("com.dilwar.multiViewAdapter", "GViewFactory")
    val classGViewLayout = ClassName("com.dilwar.multiViewAdapter", "GViewLayout")
    val classViewGroup = ClassName("android.view", "ViewGroup")
    val classLayoutInflater = ClassName("android.view", "LayoutInflater")
    val classViewDataBinding = ClassName("androidx.databinding", "ViewDataBinding")
    val classDataBindingUtil = ClassName("androidx.databinding", "DataBindingUtil")
    val GenerationPackage = "dilwar.GRecycler"

    fun getClassName(typeMirror: TypeMirror): ClassName {
        return ClassName(getPackageName(typeMirror), getOnlyClassName(typeMirror))

    }

    fun getPackageName(typeMirror: TypeMirror): String {
        val fullName = typeMirror.toString()
        val split = fullName.split(".")

        val className = split.last()
        val packageName = fullName.replace(".$className", "")

        return packageName

    }

    fun getOnlyClassName(typeMirror: TypeMirror): String {
        val fullName = typeMirror.toString()
        val split = fullName.split(".")
        return split.last()

    }
}