package com.evacuees.check

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintFix
import com.evacuees.check.AdapterIssueDetector.Companion.G_VIEW_TYPE_ANNOTATION
import com.evacuees.check.AdapterIssueDetector.Companion.INTEGER_CLASS_REF
import com.intellij.lang.jvm.JvmModifier
import com.intellij.lang.jvm.types.JvmPrimitiveType
import com.intellij.psi.*
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.kotlin.KotlinUClass
import org.jetbrains.uast.namePsiElement
import kotlin.reflect.jvm.jvmName

class GRecyclerViewTypeHandler(private val context: JavaContext) : UElementHandler() {
    override fun visitClass(node: UClass) {
        /*   context.report(
               IssueChecker.G_RECYCLER_VIEW_TYPE_ISSUE,
               context.getNameLocation(node),
               "INCORRECT_READ_RESOLVE_SIGNATURE_EXPLANATION"
           )*/
        if (node is KotlinUClass) {
            val kClass = node as KotlinUClass
            val annotations = node.uAnnotations
            annotations.filter {
                it.qualifiedName?.contains(AdapterIssueDetector.G_RECYCLER_VIEW_TYPE_ANNOTATION)
                    ?: false
            }.forEach { _ ->
                if (!kClass.hasOpenKeyword()) openKeywordMissing(kClass)
                if (!kClass.implementsGViewType()) gViewTypeImplementationMissing(kClass)
                if (kClass.doesNotHaveGetViewTypeLayout()) createGetViewTypeLayout(kClass)
                else if (!kClass.signatureOfGetViewTypeLayout()) createGetViewTypeLayout(kClass.findGetViewTypeLayout()!!)
            }
        }
    }

    fun KotlinUClass.doesNotHaveGetViewTypeLayout(): Boolean = findGetViewTypeLayout() == null
    //allMethods.none { it.name == "getViewTypeLayout" }

    fun KotlinUClass.signatureOfGetViewTypeLayout(): Boolean {
        val findGetViewTypeLayout = findGetViewTypeLayout()
        //val returnClass = (findGetViewTypeLayout?.returnTypeElement?.type as PsiClassType).resolve()
        //val returnClassRef = (findGetViewTypeLayout?.returnType as? JvmPrimitiveType)?.kind?.boxedFqn//findGetViewTypeLayout?.returnType?.canonicalText
        val returnClassRef = findGetViewTypeLayout?.returnType?.canonicalText

        return returnClassRef == "int" && findGetViewTypeLayout.isStatic
    }

    fun KotlinUClass.findGetViewTypeLayout() =
        methods.firstOrNull { it.name == "getViewTypeLayout" }

    fun KotlinUClass.hasOpenKeyword() = !isFinal

    private fun KotlinUClass.implementsGViewType() =
        interfaces.find { it.isInterface && it.qualifiedName == G_VIEW_TYPE_ANNOTATION } != null


    fun createGetViewTypeLayout(uClass: UClass) {
        context.report(
            AdapterIssueDetector.G_RECYCLER_VIEW_TYPE_ISSUE,
            context.getNameLocation(uClass),
            """
                create static function with name getViewTypeLayout and return Int.
                and function must me annotated with @JvmStatic and @LayoutRes
            """.trimIndent()/*,
            LintFix.create()
                .name("Add readResolve() method")
                .replace()
                .text(LintFix.ReplaceString.INSERT_END)
                .with(" {\n@Suppress(\"UnusedPrivateMember\")\nprivate fun readResolve(): Any = as\n}")
                .reformat(true)
                .autoFix()
                .build()*/
        )
    }

    fun createGetViewTypeLayout(uMethod: PsiMethod) {
        context.report(
            AdapterIssueDetector.G_RECYCLER_VIEW_TYPE_ISSUE,
            uMethod,
            context.getLocation(uMethod),
            "function should be static with name getViewTypeLayout and return Int"
        )
    }

    fun createGetViewTypeLayout(uMethod: UMethod) {
        context.report(
            AdapterIssueDetector.G_RECYCLER_VIEW_TYPE_ISSUE,
            context.getLocation(uMethod),
            "function should be static with name getViewTypeLayout and return Int"
        )
    }

    fun openKeywordMissing(uClass: UClass) {
        context.report(
            AdapterIssueDetector.G_RECYCLER_VIEW_TYPE_ISSUE,
            context.getNameLocation(uClass),
            "open keyword missing"
        )
    }

    fun gViewTypeImplementationMissing(uClass: UClass) {
        context.report(
            AdapterIssueDetector.G_RECYCLER_VIEW_TYPE_ISSUE,
            context.getNameLocation(uClass),
            "GViewType Implementation Missing"
        )
    }
}