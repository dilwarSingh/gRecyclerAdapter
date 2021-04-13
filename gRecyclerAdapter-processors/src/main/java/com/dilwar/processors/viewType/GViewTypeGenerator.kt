package com.dilwar.processors.viewType

import android.support.annotation.LayoutRes
import com.dilwar.common.ClassGenerator
import com.dilwar.common.Constants
import com.dilwar.common.Validator
import com.dilwar.common.firstCharSmall
import com.squareup.kotlinpoet.*
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

class GViewTypeGenerator(parentClass: ClassName, filer: Filer, messager: Messager) :
    ClassGenerator(parentClass, filer, messager) {
    override fun generate(element: Element, elements: Elements?, types: Types?) {
        val viewHolderClass = TypeSpec.classBuilder(name = className())
            .addModifiers(KModifier.PUBLIC)
            .superclass(parentClass)
            .addSuperinterface(Constants.classGViewLayout)

        addConstructor(viewHolderClass, element)

        viewHolderClass.addFunction(addOnBindViewHolder())
        viewHolderClass.addFunction(getLayoutId())
        buildClass(viewHolderClass.build())

    }

    override fun className() = parentClass.simpleName + Constants.VIEW_TYPE_SUFFIX

    private fun getLayoutId(): FunSpec {
        return FunSpec.builder("getViewType")
            .addAnnotation(LayoutRes::class)
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
            .returns(Int::class.java)
            .addStatement(
                "return %T.${parentClass.simpleName.firstCharSmall()}",
                Constants.GResourcesClass
            )
            .build()

    }

    private fun addConstructor(cls: TypeSpec.Builder, element: Element) {
        val genricTypeName = Validator.getGenricTypeNameOf(element, Constants.classGViewType)

        cls.primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("model", genricTypeName!!)
                .build()
        ).addProperty(
            PropertySpec.builder("model", genricTypeName)
                .initializer("model")
                .build()
        )
    }

    private fun addOnBindViewHolder(): FunSpec {

        val recyclerView = ParameterSpec.builder(
            "recyclerViewHolder",
            Constants.classRecyclerView_ViewHolder
        )
            .build()

        return FunSpec
            .builder("onBindViewHolder")
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
            .addParameter(recyclerView)
            .addStatement("super.onBindViewHolder(recyclerViewHolder,model)")
            .build()
    }
}