package com.uniquestudio

/**
 * Created by coxier on 17-4-12.
 */
class MethodInfo {
    String methodSignature
    String usecs
    // ent or xit
    String action

    def children

    @Override
    boolean equals(Object obj) {
        MethodInfo another = (MethodInfo)obj
        return usecs.equals(another.usecs)
    }

    @Override
    String toString() {
        return "${action}\t${usecs}\t${methodSignature}"
    }
}
