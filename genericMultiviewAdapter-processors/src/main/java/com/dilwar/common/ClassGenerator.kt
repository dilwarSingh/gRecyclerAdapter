package com.dilwar.common

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.IOException
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

abstract class ClassGenerator(
    val parentClass: ClassName,
    private val filer: Filer,
    private val messager: Messager
) {
    abstract fun generate(element: Element)
    abstract fun className(): String
    open fun packageAddress() = "${Constants.GenerationPackage}.${parentClass.packageName}"
    fun buildClass(typeSpec: TypeSpec) {
        try {
            FileSpec.builder(packageAddress(), className())
                .addType(typeSpec)
                .build()
                .writeTo(filer)
        } catch (e: IOException) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.message)
            e.printStackTrace()
        }
    }
}