package com.evacuees.check

import com.android.tools.lint.detector.api.*
import com.android.tools.lint.detector.api.Category.Companion.USABILITY
import com.android.tools.lint.detector.api.Scope.Companion.JAVA_FILE_SCOPE
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

@SuppressWarnings("UnstableApiUsage")
class CaimitoDetector : Detector(), SourceCodeScanner {
    companion object {
        @JvmField
        val ISSUE: Issue = Issue.create(
            "WarningAnnotation",
            "This method has been annotated with @Warning",
            "This method has special conditions surrounding it's use, be careful when using it and refer to its documentation.",
            USABILITY, 7, Severity.WARNING,
            Implementation(CaimitoDetector::class.java, JAVA_FILE_SCOPE)
        )
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        super.visitMethodCall(context, node, method)
        val evaluator = context.evaluator
        if (evaluator.isMemberInClass(method, "android.util.Log")) {
            reportUsage(context, node)
        }
    }

    private fun reportUsage(context: JavaContext, node: UCallExpression) {
        context.report(
            issue = ISSUE,
            scope = node,
            location = context.getCallLocation(
                call = node,
                includeReceiver = true,
                includeArguments = true
            ),
            message = "android.util.Log usage is forbidden."
        )
    }
}