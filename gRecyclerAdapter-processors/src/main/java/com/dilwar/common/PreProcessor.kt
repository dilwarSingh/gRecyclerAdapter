package com.dilwar.common

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import kotlin.reflect.KClass

abstract class PreProcessor(
    private val clazz: KClass<*>? = null,
    private val clazzName: String? = null
) :
    AbstractProcessor() {

    init {
        clazz ?: clazzName ?: throw NullPointerException("clazz or clazzName one must have value")
    }

    protected lateinit var filer: Filer
    protected lateinit var messager: Messager
    protected var elements: Elements? = null
    protected var types: Types? = null

    @Synchronized
    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        filer = processingEnvironment.filer!!
        messager = processingEnvironment.messager!!
        elements = processingEnvironment.elementUtils
        types = processingEnvironment.typeUtils

    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(clazz?.java?.canonicalName ?: clazzName!!)
        //  return setOf(GRecyclerViewType::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_8
    }


}