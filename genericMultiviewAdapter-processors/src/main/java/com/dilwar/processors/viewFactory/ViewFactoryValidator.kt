package com.dilwar.processors.viewFactory

import com.dilwar.hits.Constants.classGViewFactory
import com.dilwar.hits.Validator
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

object ViewFactoryValidator : Validator() {

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
                "Class doesn't implement anything implement it with GViewFactory"
            )
            return true
        } else {

            val typeName = classGViewFactory.packageName + "." + classGViewFactory.simpleName
            var foundGViewFactoryClass = false

            for (mirror in typeMirror) {
                if (mirror.toString().contains(typeName)) {
                    foundGViewFactoryClass = true
                    break
                }
            }
            if (!foundGViewFactoryClass) {
                messager!!.printMessage(
                    Diagnostic.Kind.ERROR,
                    typeMirror[0].toString() + "!===" + typeName
                )
                messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Class Must Implement GViewFactory interface."
                )
                return true
            }

        }
        return false
    }


}