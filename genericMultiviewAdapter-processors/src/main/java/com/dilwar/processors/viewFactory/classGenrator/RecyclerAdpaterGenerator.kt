package com.dilwar.processors.viewFactory.classGenrator

import com.dilwar.hits.Constants
import com.dilwar.hits.Constants.classBaseRAdapter
import com.dilwar.hits.Constants.classGViewType
import com.dilwar.hits.Constants.classRecyclerView_ViewHolder
import com.dilwar.hits.Constants.classViewGroup
import com.dilwar.hits.Validator
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.IOException
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic


class RecyclerAdpaterGenerator(val filer: Filer?, val messager: Messager?) {
    lateinit var ADAPTER_CLASS_NAME: String
    lateinit var VH_FACTORY_CLASS_NAME: String
    lateinit var VIEW_HOLDERS_CLASS_NAME: String

    lateinit var genricTypeName: TypeName
    fun generate(parentClass: ClassName, element: Element) {

        ADAPTER_CLASS_NAME = parentClass.simpleName + "Adapter"
        VH_FACTORY_CLASS_NAME = parentClass.simpleName + "VHFactory"
        VIEW_HOLDERS_CLASS_NAME = parentClass.simpleName + "ViewHolders"

        genricTypeName =
            Validator.getGenricTypeNameOf(element as TypeElement, Constants.classGViewFactory)!!


        val adapterClass = TypeSpec
            .classBuilder(ADAPTER_CLASS_NAME)
            .addModifiers(KModifier.OPEN)
            .superclass(classBaseRAdapter.parameterizedBy(genricTypeName))

        // override fun addMessageToList(receivedMessage: MessageModel): GViewType<MessageModel>

        val classFactory = ClassName(
            "${Constants.GenerationPackage}.${parentClass.packageName}",
            VH_FACTORY_CLASS_NAME
        )

        adapterClass.addFunction(
            FunSpec.builder("addMessageToList")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("receivedMessage", genricTypeName)
                .returns(classGViewType.parameterizedBy(genricTypeName))
                .addStatement("return %T.getType(receivedMessage)", classFactory)
                .build()
        )


        //  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

        adapterClass.addFunction(
            FunSpec.builder("onCreateViewHolder")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("parent", classViewGroup)
                .addParameter("viewType", Int::class)
                .returns(classRecyclerView_ViewHolder)
                .addStatement("return %T.create(parent,viewType)", classFactory)
                .build()
        )

        try {
            FileSpec.builder(
                "${Constants.GenerationPackage}.${parentClass.packageName}",
                ADAPTER_CLASS_NAME
            )
                .addType(adapterClass.build())
                .build()
                .writeTo(filer!!)
        } catch (e: IOException) {
            messager!!.printMessage(Diagnostic.Kind.ERROR, e.message)
            e.printStackTrace()
        }
    }


}
