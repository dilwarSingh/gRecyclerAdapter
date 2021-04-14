package com.dilwar.processors.viewType

import com.dilwar.annotations.GRecyclerViewType
import com.dilwar.common.PreProcessor
import com.dilwar.common.Validator
import com.dilwar.processors.viewFactory.ViewFactoryValidator
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
class GRecyclerViewTypeProcessor : PreProcessor(GRecyclerViewType::class) {

    override fun process(set: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        val elementList = mutableListOf<Element>()
        for (element in roundEnvironment.getElementsAnnotatedWith(GRecyclerViewType::class.java)) {
            val typeElement = element as TypeElement
            val activityName = typeElement.simpleName.toString()
            val packageName = elements!!.getPackageOf(typeElement).qualifiedName.toString()
            val parentClass = ClassName(packageName, activityName)

            if (ViewTypeValidator.isNotValid(messager, element)) return true

            elementList.add(element)
            GViewTypeGenerator(parentClass, filer, messager).generate(element)
        }
        if (elementList.isNotEmpty())
            GResourcesGenerator(filer, messager).generate(elementList, elements)
        return true
    }
}