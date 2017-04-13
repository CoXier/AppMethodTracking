package com.uniquestudio

/**
 * Created by coxier on 17-4-13.
 */
class HtmlGenerator {
    static js = "<script langugage=\"javascript\">\n" +
            "function toggle(e,item) {\n" +
            "  obj = document.getElementById(item);\n" +
            "  childs = obj.children;\n" +
            "  for (var i = 0; i < childs.length; i++) {\n" +
            "    if(childs[i].style.display == \"none\"){\n" +
            "      childs[i].style.display = \"block\";\n" +
            "    }else{\n" +
            "      childs[i].style.display = \"none\";\n" +
            "    }\n" +
            "  }\n" +
            "  e.stopPropagation();\n" +
            "}\n" +
            "</script>\n"

    static css = "<style type=\"text/css\">\n" +
            "ul li {\n" +
            "  font-family: courier; font-size: 13;\n" +
            "  margin-top: 8px;\n" +
            "}\n" +
            "</style>\n"


    static generate = {
        fileName, rootList ->
            File file = new File(fileName)
            file.createNewFile()
            StringBuilder builder = new StringBuilder()
            insertHead(builder)
            insertBody(builder,rootList)
            insertEnd(builder)
            file.write(builder.toString())
    }

    static insertHead(builder){
        builder.append("<!DOCTYPE html>\n" +
                "<html>\n")
        builder.append(js)
        builder.append(css)
        builder.append("<body>\n")
    }


    static insertBody(builder,rootList){
        rootList.eachWithIndex{
            it,index->
            builder.append("<ul>")
            insertLi(index,builder,it)
            builder.append("</ul>")
        }
    }

    static insertLi(index,builder,methodInfo){
        def id = "${index}"
        builder.append("<li id=\"${id}\" onClick=\"toggle(event,'${id}')\" >" +
                "${methodInfo.methodSignature}#${methodInfo.usecs}")
        if (methodInfo.children != null){
            builder.append("<ul>")
            methodInfo.children.eachWithIndex{
                it,i->
                insertLi("${index}#$i",builder,it)
            }
            builder.append("</ul>")
        }
        builder.append("</li>")
    }

    static insertEnd(builder) {
        builder.append("</body>")
        builder.append("</html>")
    }



}
