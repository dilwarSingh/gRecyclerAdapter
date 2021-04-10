package com.dilwar.processors.viewFactory.classGenrator

import com.dilwar.annotations.GRecyclerViewFactory
import com.dilwar.common.Constants
import com.dilwar.common.Constants.classView
import com.dilwar.common.Constants.classViewDataBinding
import com.dilwar.common.Constants.getOnlyClassName
import com.squareup.kotlinpoet.*
import java.io.IOException
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic


class ViewHoldersGenerator(val filer: Filer?, val messager: Messager?) {
    lateinit var VIEW_HOLDERS_CLASS_NAME: String
    lateinit var parentPackageName: String

    fun generate(parentClass: ClassName, element: Element) {
        VIEW_HOLDERS_CLASS_NAME = parentClass.simpleName + "ViewHolders"
        parentPackageName = "${Constants.GenerationPackage}.${parentClass.packageName}"


        val viewHolderClass =
            TypeSpec.classBuilder(VIEW_HOLDERS_CLASS_NAME)
                .addModifiers(KModifier.SEALED)

        val enableDataBinding =
            element.getAnnotation(GRecyclerViewFactory::class.java).enableDataBinding

        val annotationMirrors = element.annotationMirrors
        annotationMirrors.forEach { annotationMirror ->
            val elementValues = annotationMirror.elementValues
            elementValues.entries.forEach { entry ->
                val key = entry.key.simpleName.toString()
                val value = entry.value.value
                when (key) {
                    "classes" -> {
                        val typeMirrors = value as List<AnnotationValue>
                        for (typeMirror in typeMirrors) {
                            if (enableDataBinding)
                                viewHolderClass.addType(
                                    generateBindingSubClass(
                                        VIEW_HOLDERS_CLASS_NAME,
                                        typeMirror.value as TypeMirror
                                    )
                                )
                            else
                                viewHolderClass.addType(
                                    generateSubClass(
                                        VIEW_HOLDERS_CLASS_NAME,
                                        typeMirror.value as TypeMirror
                                    )
                                )
                        }
                    }
                }
            }


        }

        try {
            FileSpec.builder(parentPackageName, VIEW_HOLDERS_CLASS_NAME)
                .addType(viewHolderClass.build())
                .build()
                .writeTo(filer!!)
        } catch (e: IOException) {
            messager!!.printMessage(Diagnostic.Kind.ERROR, e.message)
            e.printStackTrace()
        }
    }

    private fun generateSubClass(parentClass: String, type: TypeMirror): TypeSpec {

        val cls = getOnlyClassName(type)
        val subClassName = "${cls}Row"
        val returnValue = ClassName("$parentPackageName.$parentClass", subClassName)
        val compan = TypeSpec.companionObjectBuilder()
            .addFunction(
                FunSpec
                    .builder("build")
                    .addParameter(ParameterSpec.builder("view", classView).build())
                    .addModifiers(KModifier.PUBLIC)
                    .returns(returnValue)
                    .addCode("return $subClassName(view)")
                    .build()
            ).build()


        return TypeSpec.classBuilder(subClassName)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("view", classView)
                    .build()
            ).addProperty(
                PropertySpec.builder("view", classView)
                    .initializer("view")
                    .build()
            )
            .superclass(Constants.classRecyclerView_ViewHolder)
            .addSuperclassConstructorParameter("view")
            .addType(compan)
            .build()
    }

    private fun generateBindingSubClass(parentClass: String, type: TypeMirror): TypeSpec {

        val cls = getOnlyClassName(type)
        val subClassName = "${cls}Row"
        val returnValue = ClassName("$parentPackageName.$parentClass", subClassName)
        val compan = TypeSpec.companionObjectBuilder()
            .addFunction(
                FunSpec
                    .builder("build")
                    .addParameter(ParameterSpec.builder("binding", classViewDataBinding).build())
                    .addModifiers(KModifier.PUBLIC)
                    .returns(returnValue)
                    .addCode("return $subClassName(binding)")
                    .build()
            ).build()


        return TypeSpec.classBuilder(subClassName)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("binding", classViewDataBinding)
                    .build()
            ).addProperty(
                PropertySpec.builder("binding", classViewDataBinding)
                    .initializer("binding")
                    .build()
            )
            .superclass(Constants.classRecyclerView_ViewHolder)
            .addSuperclassConstructorParameter("binding.root")
            .addType(compan)
            .build()
    }
}
