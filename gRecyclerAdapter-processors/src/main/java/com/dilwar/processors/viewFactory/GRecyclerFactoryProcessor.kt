package com.dilwar.processors.viewFactory

import com.dilwar.common.Constants.GRecyclerViewFactoryQName
import com.dilwar.common.PreProcessor
import com.dilwar.processors.viewFactory.classGenrator.RecyclerAdpaterGenerator
import com.dilwar.processors.viewFactory.classGenrator.VHFactoryGenerator
import com.dilwar.processors.viewFactory.classGenrator.ViewHoldersGenerator
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement


@AutoService(Processor::class)
class GRecyclerFactoryProcessor : PreProcessor(clazzName = GRecyclerViewFactoryQName) {

    override fun process(set: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        for (element in roundEnvironment.getElementsAnnotatedWith(
            elements?.getTypeElement(
                GRecyclerViewFactoryQName
            )
        )) {
            val typeElement = element as TypeElement
            val activityName = typeElement.simpleName.toString()
            val packageName = elements!!.getPackageOf(typeElement).qualifiedName.toString()

            if (ViewFactoryValidator.isNotValid(messager, element)) return true



            val parentClass = ClassName(packageName, activityName)

            ViewHoldersGenerator(parentClass, filer, messager).generate(element, elements,types)
            VHFactoryGenerator(parentClass, filer, messager).generate(element, elements, types)
            RecyclerAdpaterGenerator(parentClass, filer, messager).generate(
                element,
                elements,
                types
            )

        }
        return true
    }

}