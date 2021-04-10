package com.dilwar.processors.viewFactory.classGenrator

import com.dilwar.annotations.GRecyclerViewFactory
import com.dilwar.common.Constants
import com.dilwar.common.Constants.classDataBindingUtil
import com.dilwar.common.Constants.classGViewType
import com.dilwar.common.Constants.classLayoutInflater
import com.dilwar.common.Constants.classView
import com.dilwar.common.Constants.classViewDataBinding
import com.dilwar.common.Constants.classViewGroup
import com.dilwar.common.Constants.getOnlyClassName
import com.dilwar.common.Validator
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.IOException
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic


class VHFactoryGenerator(val filer: Filer?, val messager: Messager?) {
    lateinit var VH_CLASS_NAME: String
    lateinit var VIEW_HOLDERS_CLASS_NAME: String
    var genricTypeName: TypeName? = null
    lateinit var parentPackageName: String

    fun generate(parentClass: ClassName, element: Element) {
        VH_CLASS_NAME = parentClass.simpleName + "VHFactory"
        VIEW_HOLDERS_CLASS_NAME = parentClass.simpleName + "ViewHolders"
        parentPackageName = "${Constants.GenerationPackage}.${parentClass.packageName}"

        genricTypeName =
            Validator.getGenricTypeNameOf(element as TypeElement, Constants.classGViewFactory)

        val vhClass =
            TypeSpec.objectBuilder(VH_CLASS_NAME)
                .addModifiers(KModifier.PUBLIC)
                .superclass(parentClass)

        vhClass.addFunction(getViewMethod())

        val isBindingEnabled =
            element.getAnnotation(GRecyclerViewFactory::class.java).enableDataBinding

        if (isBindingEnabled) {
            vhClass.addFunction(getBindingViewMethod())
        }

        val annotationMirrors = element.annotationMirrors
        annotationMirrors.forEach { annotationMirror ->
            val elementValues = annotationMirror.elementValues
            elementValues.entries.forEach { entry ->
                val key = entry.key.simpleName.toString()
                val value = entry.value.value
                when (key) {
                    "classes" -> {
                        val typeMirrors = value as List<AnnotationValue>

                        vhClass.addFunction(
                            createMethod(VH_CLASS_NAME, typeMirrors, isBindingEnabled)
                        )
                        vhClass.addFunction(
                            getRowTypeMethod(VH_CLASS_NAME, typeMirrors)
                        )

                    }
                }
            }
        }


        try {
            FileSpec.builder(
                "${Constants.GenerationPackage}.${parentClass.packageName}",
                VH_CLASS_NAME
            )
                .addType(vhClass.build())
                .build()
                .writeTo(filer!!)
        } catch (e: IOException) {
            messager!!.printMessage(Diagnostic.Kind.ERROR, e.message)
            e.printStackTrace()
        }
    }

    private fun getBindingViewMethod(): FunSpec {

        /*
 private fun getBindingView(parent: ViewGroup, layout: Int): ViewDataBinding =
        DataBindingUtil.bind(LayoutInflater.from(parent.context).inflate(layout, parent, false))!!

* */

        return FunSpec.builder("getBindingView")
            .addModifiers(KModifier.PRIVATE)
            .addParameter(ParameterSpec.builder("parent", classViewGroup).build())
            .addParameter(ParameterSpec.builder("layout", Int::class).build())
            .returns(classViewDataBinding)
            .addStatement(
                "return %T.bind(getView(parent, layout))!!",
                classDataBindingUtil
            )
            .build()
    }

    private fun getViewMethod(): FunSpec {
        return FunSpec.builder("getView")
            .addModifiers(KModifier.PRIVATE)
            .addParameter(ParameterSpec.builder("parent", classViewGroup).build())
            .addParameter(ParameterSpec.builder("layout", Int::class).build())
            .returns(classView)
            .addStatement(
                "return %T.from(parent.context).inflate(layout, parent, false)",
                classLayoutInflater
            )
            .build()
    }

    private fun createMethod(
        parentClass: String,
        typeMirrors: List<AnnotationValue>,
        enabledDataBinding: Boolean
    ): FunSpec {

        val codeBuilder = CodeBlock.builder()
        codeBuilder.beginControlFlow("return when(viewType)")

        for (typeMirror in typeMirrors) {
            val type = typeMirror.value as TypeMirror
            val cls = getOnlyClassName(type)
            val packageName = "${Constants.GenerationPackage}.${Constants.getPackageName(type)}"
            val classRowName = "${cls}Row"

            val classGViewTypeName = "${cls}GViewType"
            val classRow =
                ClassName("$parentPackageName.$VIEW_HOLDERS_CLASS_NAME", classRowName)
            val classGViewType = ClassName(packageName, classGViewTypeName)

            codeBuilder.beginControlFlow(
                "%T.getLayout()->",
                classGViewType
            )

            codeBuilder.addStatement(
                if (enabledDataBinding)
                    "%T.build(getBindingView(parent,%T.getLayout()))"
                else
                    "%T.build(getView(parent,%T.getLayout()))", classRow,
                classGViewType
            ).endControlFlow()

        }

        codeBuilder.addStatement(
            "else -> throw %T(%S)",
            IllegalArgumentException::class,
            "Undefined ViewType"
        )
        codeBuilder.endControlFlow()


        return FunSpec.builder("create")
            .addParameter(ParameterSpec.builder("parent", classViewGroup).build())
            .addParameter(ParameterSpec.builder("viewType", Int::class).build())
            .returns(Constants.classRecyclerView_ViewHolder)
            .addCode(codeBuilder.build())
            .build()


    }

    private fun getRowTypeMethod(vhClassName: String, typeMirrors: List<AnnotationValue>): FunSpec {

        val codeBuilder = CodeBlock.builder()
        //  codeBuilder.add("val viewType = getViewTypeLayoutId(data)")
        codeBuilder.addStatement("val viewType = getViewTypeLayoutId(data)")
        codeBuilder.beginControlFlow("return when(viewType)")

        for (typeMirror in typeMirrors) {
            val type = typeMirror.value as TypeMirror
            val cls = getOnlyClassName(type)
            val classGViewTypeName = "${cls}GViewType"
            val packageName = "${Constants.GenerationPackage}.${Constants.getPackageName(type)}"
            val classGViewType = ClassName(packageName, classGViewTypeName)

            codeBuilder.beginControlFlow("%T.getLayout() ->", classGViewType)
                .addStatement("%T(data)", classGViewType)
                .endControlFlow()
        }

        codeBuilder.addStatement(
            "else -> throw %T(%S)",
            IllegalArgumentException::class,
            "Un-attached Layout"
        ).endControlFlow()

        return FunSpec.builder("getType")
            .returns(classGViewType.parameterizedBy(genricTypeName!!))
            .addParameter(ParameterSpec.builder("data", genricTypeName!!).build())
            .addCode(codeBuilder.build())
            .build()

    }

}
