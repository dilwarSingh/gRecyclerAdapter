package com.dilwar.processors.viewType

import com.dilwar.common.Constants
import com.dilwar.common.Validator
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

object ViewTypeValidator : Validator() {

    fun isNotValid(messager: Messager?, element: Element): Boolean {
        val typeElement = element as TypeElement

        for (modifier in element.modifiers) {
            if (modifier == Modifier.FINAL) {
                messager!!.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Class Can't be final if using `kotlin` make sure you uses `open` keyword with your class"
                )

            }
        }


        if (element.kind != ElementKind.CLASS) {
            messager!!.printMessage(Diagnostic.Kind.ERROR, "Can be applied to class.")
            return true
        }

        val typeMirror = typeElement.interfaces
        if (typeMirror.isEmpty()) {
            messager!!.printMessage(
                Diagnostic.Kind.ERROR,
                "Class must implement GViewType interface"
            )
            return true
        } else {

            val typeName =
                Constants.classGViewType.packageName + "." + Constants.classGViewType.simpleName
            var foundGViewTypeClass = false

            for (mirror in typeMirror) {
                if (mirror.toString().contains(typeName)) {
                    foundGViewTypeClass = true
                    break
                }
            }
            if (!foundGViewTypeClass) {
                messager!!.printMessage(
                    Diagnostic.Kind.ERROR,
                    typeMirror[0].toString() + "!=" + typeName
                )
                messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Class Must Implement GViewType interface"
                )
                return true
            }

        }
        return false
    }

}