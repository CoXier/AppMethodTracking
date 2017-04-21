package com.uniquestudio

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.*

/**
 * Created by coxier on 17-4-16.
 */
class MethodTrackingTest {
    @Test
    void canAddTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('tacking', type: MethodTrackingTask)
        assertTrue(task instanceof MethodTrackingTask)
    }

    @Test
    void filterMethod() {
        String traceInfo = "VERSION: 3\n" +
                "Threads (13):\n" +
                "7118 main\n" +
                "7124 Signal Catcher\n" +
                "7125 JDWP\n" +
                "7126 ReferenceQueueDaemon\n" +
                "7134 FinalizerDaemon\n" +
                "7135 FinalizerWatchdogDaemon\n" +
                "7136 HeapTaskDaemon\n" +
                "7137 Binder_1\n" +
                "7138 Binder_2\n" +
                "7155 RenderThread\n" +
                "7159 hwuiTask1\n" +
                "7160 hwuiTask2\n" +
                "7164 Binder_3\n" +
                "Trace (threadID action usecs class.method signature):\n" +
                "7118 xit         0 ..dalvik.system.VMDebug.startMethodTracingFilename (Ljava/lang/String;IIZI)V VMDebug.java\n" +
                "7118 xit      1869 .dalvik.system.VMDebug.startMethodTracing (Ljava/lang/String;IIZI)V  VMDebug.java\n" +
                "7118 xit      1880 android.os.Debug.startMethodTracing (Ljava/lang/String;II)V  Debug.java\n" +
                "7118 xit      1888-android.os.Debug.startMethodTracing (Ljava/lang/String;)V    Debug.java\n" +
                "7118 ent      1907-com.hackerli.sample.MainActivity.wearCloth ()V       MainActivity.java\n" +
                "7118 ent      1916 com.hackerli.sample.MainActivity.putOnCoat ()V       MainActivity.java\n" +
                "7118 xit      1922 com.hackerli.sample.MainActivity.putOnCoat ()V       MainActivity.java\n" +
                "7118 ent      1927 com.hackerli.sample.MainActivity.putOnPants ()V      MainActivity.java\n" +
                "7118 xit      1930 com.hackerli.sample.MainActivity.putOnPants ()V      MainActivity.java\n" +
                "7118 ent      1935 com.hackerli.sample.MainActivity.putOnShoes ()V      MainActivity.java\n" +
                "7118 ent      1940 .com.hackerli.sample.MainActivity.putOnSocks ()V     MainActivity.java\n" +
                "7118 xit      1946 .com.hackerli.sample.MainActivity.putOnSocks ()V     MainActivity.java\n" +
                "7118 xit      1949 com.hackerli.sample.MainActivity.putOnShoes ()V      MainActivity.java\n" +
                "7118 ent      1954 com.hackerli.sample.MainActivity.putOnHat ()V        MainActivity.java\n" +
                "7118 xit      1958 com.hackerli.sample.MainActivity.putOnHat ()V        MainActivity.java\n" +
                "7118 xit      1961-com.hackerli.sample.MainActivity.wearCloth ()V       MainActivity.java\n" +
                "7118 ent      1966-android.os.Debug.stopMethodTracing ()V       Debug.java\n" +
                "7118 ent      1982 dalvik.system.VMDebug.stopMethodTracing ()V  VMDebug.java"

        Project project = ProjectBuilder.builder().build()
        def task = (MethodTrackingTask) project.task('tacking', type: MethodTrackingTask)
        task.filterList = ['com.hackerli.sample']

        List<MethodInfo> filteredMethod = task.parseMethodInfo(traceInfo).toList()

        assertNotNull(filteredMethod)
        assertNotEquals(filteredMethod.size, 0)

        for (MethodInfo it : filteredMethod) {
            assertTrue(it.action == "ent" || it.action == "xit")
            assertTrue(it.methodSignature == "com.hackerli.sample.MainActivity.putOnHat"
                    || it.methodSignature == "com.hackerli.sample.MainActivity.putOnSocks"
                    || it.methodSignature == "com.hackerli.sample.MainActivity.putOnShoes"
                    || it.methodSignature == "com.hackerli.sample.MainActivity.putOnCoat"
                    || it.methodSignature == "com.hackerli.sample.MainActivity.putOnPants"
                    || it.methodSignature == "com.hackerli.sample.MainActivity.wearCloth")
        }


    }

}
