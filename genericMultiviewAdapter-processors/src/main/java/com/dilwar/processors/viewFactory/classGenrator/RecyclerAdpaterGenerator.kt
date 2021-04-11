package com.dilwar.processors.viewFactory.classGenrator

import com.dilwar.common.ClassGenerator
import com.dilwar.common.Constants
import com.dilwar.common.Constants.classBaseRAdapter
import com.dilwar.common.Constants.classGViewType
import com.dilwar.common.Constants.classRecyclerView_ViewHolder
import com.dilwar.common.Constants.classViewGroup
import com.dilwar.common.Validator
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements


class RecyclerAdpaterGenerator(parentClass: ClassName, filer: Filer, messager: Messager) :
    ClassGenerator(parentClass, filer, messager) {
    //lateinit var ADAPTER_CLASS_NAME: String
    lateinit var VH_FACTORY_CLASS_NAME: String
    lateinit var VIEW_HOLDERS_CLASS_NAME: String

    lateinit var genricTypeName: TypeName
    override fun generate(element: Element) {

        VH_FACTORY_CLASS_NAME = parentClass.simpleName + "VHFactory"
        VIEW_HOLDERS_CLASS_NAME = parentClass.simpleName + "ViewHolders"

        genricTypeName =
            Validator.getGenricTypeNameOf(element as TypeElement, Constants.classGViewFactory)!!


        val adapterClass = TypeSpec
            .classBuilder(className())
            .addModifiers(KModifier.OPEN)
            .superclass(classBaseRAdapter.parameterizedBy(genricTypeName))

        // override fun addMessageToList(receivedMessage: MessageModel): GViewType<MessageModel>

        val classFactory = ClassName(
            packageAddress(),
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

        buildClass(adapterClass.build())
    }

    override fun className() = parentClass.simpleName + "Adapter"
}
