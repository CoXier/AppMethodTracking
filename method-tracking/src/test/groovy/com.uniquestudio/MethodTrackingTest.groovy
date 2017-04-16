package com.uniquestudio

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * Created by coxier on 17-4-16.
 */
class MethodTrackingTest {
    @Test
    public void canAddTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('tacking', type: MethodTrackingTask)
        assertTrue(task instanceof MethodTrackingTask)
    }
}
