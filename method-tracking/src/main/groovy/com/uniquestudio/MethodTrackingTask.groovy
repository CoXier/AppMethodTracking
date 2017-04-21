package com.uniquestudio

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by coxier on 17-4-10.
 */
class MethodTrackingTask extends DefaultTask {
    String group = 'MethodTracking'

    def projectDirPath

    String traceInfo
    def traceName
    def filterList

    def filteredMethod

    int start, end

    @TaskAction
    track() {
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
        filterList = project.methodTracking.filterList
        if (traceName == null) throw new RuntimeException('Trace name cannot be null')
        if (filterList == null) throw new RuntimeException('At least one filter is needed')

        projectDirPath = project.getProjectDir().getParentFile().path + File.separator
        def platformPath = project.android.getSdkDirectory().toString() + '/platform-tools'

        // pull trace file from devise
        def adb = platformPath + '/adb'
        def pullCommand = "${adb} pull /sdcard/${traceName} ${projectDirPath}${traceName}"
        println "${pullCommand}"
        String os = System.getProperty("os.name").toLowerCase()
        if (os.contains("windows"))
            ("cmd /c start /b ${pullCommand}").execute().waitFor()
        else
            ['bash', '-c', pullCommand].execute().waitFor()

        File file = new File("${projectDirPath}${traceName}")
        if (!file.exists()) throw new RuntimeException("Trace name may be invalid or devices offline")

        // dump trace file
        def dmtracedump = "${platformPath}/dmtracedump"
        def dmCommand = "${dmtracedump}  -o  ${projectDirPath}${traceName}"
        println "${dmCommand}"
        if (os.contains("windows"))
            ("cmd /c start /b ${dmCommand}").execute().text
        else
            ['bash', '-c', dmCommand].execute().text
    }

    /**
     * parseMethod trace info to html.
     * If method A invokes B and C,we call A is root of B and C.
     * Obviously after parsing ,we will get some {@link MethodInfo}s
     */
    def parseTraceInfo() {
        println '\nSTART PARSING\n'

        filteredMethod = parseMethodInfo(traceInfo)
        filteredMethod.each { println(it) }

        def rootList = []
        start = 0
        end = filteredMethod.size()
        while (start < end) {
            MethodInfo root = search()
            rootList.add(root)
        }
        println('\n')
        rootList.each {
            printMethod(0, it)
        }

        // generate html
        def generator = HtmlGenerator.generate
        generator(projectDirPath + "${traceName}.html", rootList)

        println '\nFINISHED'
    }

    /**
     * extract all methods from traceInfo
     * @param traceInfo
     * @return
     */
    def parseMethodInfo(traceInfo){
        String[] traceList = traceInfo.split('\n')
        int i
        for (i = 0; i < traceList.length; i++) {
            String s = traceList[i]
            // methods appear
            if (s == "Trace (threadID action usecs class.method signature):") {
                i++
                break
            }
        }

        filteredMethod = []

        // filter method by package name
        while (i < traceList.length) {
            def info = Utils.parseMethod(traceList[i])
            filterList.each {
                if (info.get(2) == it) {
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
        println(filteredMethod.class)
        filteredMethod
    }


    MethodInfo search() {
        int k = find()
        MethodInfo root = filteredMethod.get(k)
        root.usecs = (filteredMethod.get(k).usecs as int) - (filteredMethod.get(start).usecs as int)

        start++
        def children = []
        while (start < k) {
            children.add(search())
        }
        start = k + 1
        root.children = children
        root
    }

    int find() {
        int k = start + 1
        int count = 1
        MethodInfo kM = filteredMethod.get(k)
        MethodInfo sM = filteredMethod.get(start)

        while (true) {
            if (kM.methodSignature == sM.methodSignature) {
                if (kM.action == 'ent') count++
                else count--

                if (count == 0) break
            }
            kM = filteredMethod.get(++k)
        }
        k
    }

    def printMethod(int i, MethodInfo info) {
        def blank = ''
        int j = 0
        while (j < i) {
            blank = blank + '\t'
            j++
        }
        println("${blank}${info.methodSignature}#${info.usecs}")
        def children = info.children
        children.eachWithIndex {
            item, index ->
                printMethod(i + 1, item)
        }
    }

}
