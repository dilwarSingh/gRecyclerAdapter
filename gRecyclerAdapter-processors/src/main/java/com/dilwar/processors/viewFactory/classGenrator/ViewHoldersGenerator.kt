package com.dilwar.processors.viewFactory.classGenrator

import com.dilwar.common.ClassGenerator
import com.dilwar.common.Constants
import com.dilwar.common.Constants.classView
import com.dilwar.common.Constants.classViewDataBinding
import com.dilwar.common.Constants.getOnlyClassName
import com.squareup.kotlinpoet.*
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types


class ViewHoldersGenerator(parentClass: ClassName, filer: Filer, messager: Messager) :
    ClassGenerator(parentClass, filer, messager) {

    override fun generate(element: Element, elements: Elements?, types: Types?) {
        val viewHolderClass =
            TypeSpec.classBuilder(className())
                .addModifiers(KModifier.SEALED)

        /*
        val enableDataBinding =
            element.getAnnotation(GRecyclerViewFactory::class.java).enableDataBinding
*/
        val enableDataBinding = true
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
                                        className(),
                                        typeMirror.value as TypeMirror
                                    )
                                )
                            else
                                viewHolderClass.addType(
                                    generateSubClass(
                                        className(),
                                        typeMirror.value as TypeMirror
                                    )
                                )
                        }
                    }
                }
            }


        }

        buildClass(viewHolderClass.build())
    }

    override fun className() = parentClass.simpleName + "ViewHolders"

    private fun generateSubClass(parentClass: String, type: TypeMirror): TypeSpec {

        val cls = getOnlyClassName(type)
        val subClassName = "${cls}Row"
        val returnValue = ClassName("${packageAddress()}.$parentClass", subClassName)
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
        val returnValue = ClassName("${packageAddress()}.$parentClass", subClassName)
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
