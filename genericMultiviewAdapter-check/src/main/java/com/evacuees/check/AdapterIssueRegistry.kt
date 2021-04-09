package com.evacuees.check

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue
import com.evacuees.check.AdapterIssueDetector.Companion.G_RECYCLER_VIEW_TYPE_ISSUE

@SuppressWarnings(value = ["UnstableApiUsage"])
class AdapterIssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(G_RECYCLER_VIEW_TYPE_ISSUE)
}