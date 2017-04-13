package com.uniquestudio
/**
 * Created by coxier on 17-4-12.
 */
class Utils {
    /**
     *
     * @param s
     * @return List format is  [ext/ent,usecs,packageName,methodSignature]
     */
    static def parseMethod(String s) {
        def result = []
        int start = 0, end = 0
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ' as char) {
                start = i + 1
                break
            }
        }

        // ext or ent
        for (int i = start; i < s.length(); i++) {
            if (s.charAt(i) == ' ' as char) {
                end = i
                result.add(s.subSequence(start, end))
                start = end
                break
            }
        }

        for (int i = start; i < s.length(); i++) {
            if (s.charAt(i) == ' ' as char) continue
            start = i
            break
        }

        // usesc
        for(int i= start;i<s.length();i++){
            char c = s.charAt(i)
            if (c >= ('0' as char) && c<= ('9' as char) ) continue
            end = i
            result.add(s.subSequence(start,end))
            start = end
            break
        }

        // methodSignature
        for (int i= start;i<s.length();i++){
            char c = s.charAt(i)
            if(c != (' ' as char) && c!= ('.' as char) && c!= ('-' as char)) {
                start = i
                break
            }
        }

        int count = 0
        for(int i=start;i<s.length();i++){
            char c = s.charAt(i)
            if (c == (' ' as char)){
                end = i
                result.add(s.substring(start,end))
            }else if(c == ('.' as char)){
                count++
                if(count == 3){
                    result.add(s.substring(start,i))
                }
            }
        }

        return result
    }

}
