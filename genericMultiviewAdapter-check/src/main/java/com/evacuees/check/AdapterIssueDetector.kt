package com.evacuees.check

import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.*


@SuppressWarnings(value = ["UnstableApiUsage"])
class AdapterIssueDetector : Detector(), SourceCodeScanner {
    companion object {
        @JvmStatic
        val G_RECYCLER_VIEW_TYPE_ISSUE: Issue = Issue.create(
            "CarefulNow",
            "Be careful when using this method.",
            "This method has special conditions surrounding it's use," +
                    " be careful when calling it and refer to its documentation.",
            Category.CORRECTNESS,
            9,
            Severity.ERROR,
            Implementation(
                AdapterIssueDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
        val G_RECYCLER_VIEW_TYPE_ANNOTATION = "com.dilwar.annotations.GRecyclerViewType"
        val G_VIEW_TYPE_ANNOTATION = "com.dilwar.multiViewAdapter.GViewType"
        val INTEGER_CLASS_REF = "java.lang.Integer"
    }


    override fun getApplicableUastTypes() = listOf(UClass::class.java)

    override fun createUastHandler(context: JavaContext) = GRecyclerViewTypeHandler(context)

}