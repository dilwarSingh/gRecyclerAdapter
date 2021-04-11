package com.dilwar.processors.viewFactory

import com.dilwar.annotations.GRecyclerViewFactory
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
class GRecyclerFactoryProcessor : PreProcessor(GRecyclerViewFactory::class) {

    override fun process(set: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        for (element in roundEnvironment.getElementsAnnotatedWith(GRecyclerViewFactory::class.java)) {
            val typeElement = element as TypeElement
            val activityName = typeElement.simpleName.toString()
            val packageName = elements!!.getPackageOf(typeElement).qualifiedName.toString()

            if (ViewFactoryValidator.isNotValid(messager, element)) return true

            val parentClass = ClassName(packageName, activityName)

            ViewHoldersGenerator(parentClass,filer, messager).generate(element)
            VHFactoryGenerator(parentClass,filer, messager).generate(element)
            RecyclerAdpaterGenerator(parentClass,filer, messager).generate(element)

        }
        return true
    }

}