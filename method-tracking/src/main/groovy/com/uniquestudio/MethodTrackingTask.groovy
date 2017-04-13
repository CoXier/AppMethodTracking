package com.uniquestudio

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
/**
 * Created by coxier on 17-4-10.
 */
class MethodTrackingTask extends DefaultTask{
    String group = 'MethodTracking'

    String traceInfo
    def traceName
    def filterList

    def filteredMethod

    @TaskAction
    def track(){
        traceInfo = generateTrace()
        println traceInfo
        parseTraceInfo()
    }

    /**
     * dump trace file whose name is set in methodTrack extension.
     * @return
     */
    def generateTrace() {
        traceName = project.methodTracking.traceName
        filterList =project.methodTracking.filterList
        if (traceName == null) throw new RuntimeException('Trace name cannot be null')
        if(filterList == null) throw new RuntimeException('At least one filter is needed')

        def projectDirPath = project.getProjectDir().getParentFile().path + File.separator
        def platformPath = project.android.getSdkDirectory().toString() + '/platform-tools'

        // pull trace file from devise
        def adb = platformPath + '/adb'
        def pullCommand = "${adb} pull /sdcard/${traceName} ${projectDirPath}"
        println "${pullCommand}"
        ['bash', '-c', pullCommand].execute().waitFor()

        // dump trace file
        def dmtracedump = "${platformPath}/dmtracedump"
        def dmCommand = "${dmtracedump}  -o  ${projectDirPath}${traceName}"
        println "${dmCommand}"
        ['bash', '-c', dmCommand].execute().text
    }

    /**
     * parse trace info to html.
     * If method A invokes B and C,we call A is root of B and C.
     * Obviously after parsing ,we will get some {@link MethodInfo}s
     */
    def parseTraceInfo(){
        println '\nSTART PARSING\n'
        String[] traceList = traceInfo.split('\n')
        int i
        for (i=0;i<traceList.length;i++){
            String s = traceList[i]
            // methods appear
            if (s == "Trace (threadID action usecs class.method signature):") {
                i++
                break
            }
        }

        filteredMethod = []

        while(i<traceList.length){
            def info = Utils.parse(traceList[i])
            filterList.each{
                if(info.get(2) == it){
                    MethodInfo methodInfo = new MethodInfo()
                    methodInfo.action = info.get(0)
                    methodInfo.usecs = info.get(1)
                    methodInfo.methodSignature = info.get(3)
                    filteredMethod.add(methodInfo)
                    // break
                    return true
                }
            }
            i++
        }

        filteredMethod.each {println(it)}



        println '\nFINISHED'
    }

}
