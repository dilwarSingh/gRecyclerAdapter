package com.dilwar.common

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.util.Elements
import kotlin.reflect.KClass

abstract class PreProcessor(val clazz: KClass<*>) : AbstractProcessor() {

    protected lateinit var filer: Filer
    protected lateinit var messager: Messager
    protected var elements: Elements? = null

    @Synchronized
    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        filer = processingEnvironment.filer!!
        messager = processingEnvironment.messager!!
        elements = processingEnvironment.elementUtils
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(clazz.java.canonicalName)
        //  return setOf(GRecyclerViewType::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_8
    }


}