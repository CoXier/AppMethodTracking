package com.uniquestudio

import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 * Created by coxier on 17-4-10.
 */
class MethodPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        project.task('track',type:MethodTrackingTask)
    }
}
