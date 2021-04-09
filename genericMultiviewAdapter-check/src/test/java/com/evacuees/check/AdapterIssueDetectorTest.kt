package com.evacuees.check

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.junit.Test

class AdapterIssueDetectorTest : LintDetectorTest() {

    val GRECYCLER_VIEW_TYPE = kotlin(
        """
            package com.dilwar.annotations
 
            @Retention(AnnotationRetention.RUNTIME)
            @Target(AnnotationTarget.CLASS)
            annotation class GRecyclerViewType(@LayoutRes val layout: Int)
        """
    ).indented()

    val GVIEW_TYPE = kotlin(
        """
            package com.dilwar.multiViewAdapter

            interface GViewType
        """
    ).indented()


    @Test
    fun testShouldDetectUsageOfAndroidLog() {
        val stubFile = kotlin(
            """
            package com.sample.lint

            import com.dilwar.annotations.GRecyclerViewType
            import com.dilwar.multiViewAdapter.GViewType
            
            @GRecyclerViewType
            open class TestType : GViewType{
                  
        @JvmStatic
        fun getViewTypeLayout():Int = 5
                     
            }
        """
        ).indented()

        lint()
            .files(
                GRECYCLER_VIEW_TYPE,
                GVIEW_TYPE,
                stubFile
            )
            .run()
            .expectErrorCount(1)

    }

    override fun getDetector(): Detector = AdapterIssueDetector()

    override fun getIssues(): MutableList<Issue> =
        mutableListOf(AdapterIssueDetector.G_RECYCLER_VIEW_TYPE_ISSUE)

}