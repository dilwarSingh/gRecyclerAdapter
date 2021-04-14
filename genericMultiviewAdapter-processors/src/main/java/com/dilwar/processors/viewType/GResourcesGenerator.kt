package com.dilwar.processors.viewType

import com.dilwar.common.Constants
import com.dilwar.common.firstCharSmall
import com.squareup.kotlinpoet.*
import java.io.IOException
import java.lang.Exception
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

class GResourcesGenerator(
    private val filer: Filer,
    private val messager: Messager
) {
    fun generate(elementList: List<Element>, elements: Elements?) {
        val resClass = TypeSpec.objectBuilder(name = className())
            .addModifiers(KModifier.PUBLIC)

        for ((index, element) in elementList.withIndex()) {
            val typeElement = element as TypeElement
            val activityName = typeElement.simpleName.toString()
            val packageName = elements!!.getPackageOf(typeElement).qualifiedName.toString()
            val parentClass = ClassName(packageName, activityName)

            val objectName = parentClass.simpleName.firstCharSmall()
            resClass.addProperty(
                PropertySpec.builder(objectName, Int::class, KModifier.CONST)
                    .initializer("%L", index)
                    .build()
            )
        }
        try {
            val name = Class.forName(packageAddress() + "." + className())
        } catch (e: Exception) {
            buildClass(resClass.build())
        }
    }

    fun className() = Constants.GResourcesClassName

    fun packageAddress() = Constants.GResourcePackage

    fun buildClass(typeSpec: TypeSpec) {
        try {
            FileSpec.builder(packageAddress(), className())
                .addType(typeSpec)
                .build()
                .writeTo(filer)
        } catch (e: IOException) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.message)
            e.printStackTrace()
        } catch (e: Exception) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.message)
            e.printStackTrace()
        }
    }
}