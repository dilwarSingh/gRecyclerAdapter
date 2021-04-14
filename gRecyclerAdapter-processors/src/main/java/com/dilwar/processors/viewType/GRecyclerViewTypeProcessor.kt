package com.dilwar.processors.viewType

import com.dilwar.common.Constants.GRecyclerViewTypeQName
import com.dilwar.common.PreProcessor
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
class GRecyclerViewTypeProcessor : PreProcessor(clazzName = GRecyclerViewTypeQName) {

    override fun process(set: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        val elementList = mutableListOf<Element>()

        for (element in roundEnvironment.getElementsAnnotatedWith(elements?.getTypeElement(GRecyclerViewTypeQName))) {
            val typeElement = element as TypeElement
            val activityName = typeElement.simpleName.toString()
            val packageName = elements!!.getPackageOf(typeElement).qualifiedName.toString()
            val parentClass = ClassName(packageName, activityName)

            if (ViewTypeValidator.isNotValid(messager, element)) return true

            elementList.add(element)
            GViewTypeGenerator(parentClass, filer, messager).generate(element, elements, types)
        }
        if (elementList.isNotEmpty())
            GResourcesGenerator(filer, messager).generate(elementList, elements)
        return true
    }
}