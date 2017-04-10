package com.uniquestudio

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by coxier on 17-4-10.
 */
class MethodTrackingTask extends DefaultTask{
    String group = 'MethodTracking'

    def trace

    @TaskAction
    def track(){
        trace = generateTrace()

    }

    def generateTrace() {
        def projectDirPath = project.getProjectDir().getParentFile().path + File.separator
        def platformPath = project.android.getSdkDirectory().toString() + '/platform-tools/'
        def adb = platformPath + '/adb'
        def pullCommand = "${adb} pull /sdcard/com.hackerli.sample.trace " + projectDirPath
        ['bash', '-c', pullCommand].execute()

        def dmtracedump = platformPath + 'dmtracedump'
        def dmCommand = dmtracedump + ' -o ' + projectDirPath + 'com.hackerli.sample.trace'
        ['bash', '-c', dmCommand].execute().text
    }


}
