package com.dilwar.processors.viewType

import android.support.annotation.LayoutRes
import com.dilwar.annotations.GRecyclerViewType
import com.dilwar.hits.Constants
import com.dilwar.hits.Constants.GenerationPackage
import com.dilwar.hits.Constants.VIEW_TYPE_SUFFIX
import com.dilwar.hits.Constants.classGViewLayout
import com.dilwar.hits.Constants.classRecyclerView_ViewHolder
import com.dilwar.hits.PreProcessor
import com.dilwar.hits.Validator
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import java.io.IOException
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
class GRecyclerViewTypeProcessor : PreProcessor(GRecyclerViewType::class) {

    override fun process(set: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        for (element in roundEnvironment.getElementsAnnotatedWith(GRecyclerViewType::class.java)) {
            val typeElement = element as TypeElement

            if (ViewTypeValidator.isNotValid(messager, element)) return true

            val activityName = typeElement.simpleName.toString()
            val packageName = elements!!.getPackageOf(typeElement).qualifiedName.toString()
            val activityClass = ClassName(packageName, activityName)

            val parentClass = ClassName(packageName, activityName)


            val viewHolderClass = TypeSpec.classBuilder(activityName + VIEW_TYPE_SUFFIX)
                .addModifiers(KModifier.PUBLIC)
                .superclass(activityClass)
                .addSuperinterface(classGViewLayout)

            addConstructor(viewHolderClass, element)

            viewHolderClass.addType(getLayout(element))
            viewHolderClass.addFunction(addOnBindViewHolder())
            viewHolderClass.addFunction(getLayoutId(element))


            generateClassFile(parentClass, viewHolderClass)
        }
        return true
    }

    private fun getLayoutId(element: Element): FunSpec {
        return FunSpec.builder("getLayoutId")
            .addAnnotation(LayoutRes::class)
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
            .returns(Int::class.java)
            .addStatement("return getLayout()")
            .build()

    }

    private fun addConstructor(cls: TypeSpec.Builder, element: TypeElement) {
        val genricTypeName = Validator.getGenricTypeNameOf(element, Constants.classGViewType)

        cls.primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("data", genricTypeName!!)
                .build()
        ).addProperty(
            PropertySpec.builder("data", genricTypeName)
                .initializer("data")
                .build()
        )
    }

    private fun getLayout(element: Element): TypeSpec {
        val gRecyclerViewTypeAnnotation =
            element.getAnnotation(GRecyclerViewType::class.java)

        @LayoutRes
        val layout = gRecyclerViewTypeAnnotation.layout

        return TypeSpec.companionObjectBuilder().addFunction(
            FunSpec
                .builder("getLayout")
                .addAnnotation(JvmStatic::class.java)
                .addModifiers(KModifier.PUBLIC)
                .returns(Integer.TYPE)
                .addAnnotation(LayoutRes::class.java)
                .addStatement("return $layout")
                .build()
        ).build()


    }

    private fun addOnBindViewHolder(): FunSpec {

        val recyclerView = ParameterSpec.builder("recyclerViewHolder", classRecyclerView_ViewHolder)
            .build()

        return FunSpec
            .builder("onBindViewHolder")
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
            .addParameter(recyclerView)
            .addStatement("super.onBindViewHolder(recyclerViewHolder,data)")
            .build()
    }

    private fun generateClassFile(
        className: ClassName,
        viewHolderClass: TypeSpec.Builder
    ) {
        try {
            FileSpec.builder(
                "$GenerationPackage.${className.packageName}",
                className.simpleName + VIEW_TYPE_SUFFIX
            )
                .addType(viewHolderClass.build())
                .build()
                .writeTo(filer!!)
        } catch (e: IOException) {
            messager!!.printMessage(Diagnostic.Kind.ERROR, e.message)
            e.printStackTrace()
        }
    }
}