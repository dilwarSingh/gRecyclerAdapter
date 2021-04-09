package com.evacuees.check

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.CustomIssueRegistry
import com.android.tools.lint.detector.api.Issue
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class AdapterIssueRegistryTest {

    private var registry: AdapterIssueRegistry? = null

    @Before
    fun setUp() {
        registry = AdapterIssueRegistry()
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun testNumberOfIssues() {
        val size: Int = registry?.issues?.size ?: 0
        assertThat(size).isEqualTo(1)
    }

    @Test
    @Throws(Exception::class)
    fun testGetIssues() {
        val actual: List<Issue> = registry?.issues ?: emptyList()
        assertThat(actual).contains(AdapterIssueDetector.G_RECYCLER_VIEW_TYPE_ISSUE)
    }
}