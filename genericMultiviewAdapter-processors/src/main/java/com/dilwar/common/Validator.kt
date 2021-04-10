package com.dilwar.common

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType

abstract class Validator {

    companion object {
        fun getGenricTypeNameOf(typeElement: TypeElement, className: ClassName): TypeName? {
            val typeMirror = typeElement.interfaces
            val typeName = className.packageName + "." + className.simpleName
            for (mirror in typeMirror) {
                if (mirror.toString().contains(typeName)) {
                    val declaredType = mirror as DeclaredType
                    return declaredType.typeArguments[0].asTypeName()
                }
            }
            return null
        }
    }
}