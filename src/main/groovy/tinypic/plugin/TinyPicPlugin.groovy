/**
 * Created by boyue
 * Copyright (c) 2015-present, mogujie.
 * All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */


package tinypic.plugin

import com.android.build.gradle.AppPlugin
import com.tinify.*
import org.gradle.api.Plugin
import org.gradle.api.Project

import java.lang.Exception

class TinyPicPlugin implements Plugin<Project> {

    TinyInfo tinyinfo;

    @Override
    void apply(Project project) {

        def hasApp = project.plugins.withType(AppPlugin)

        def variants = hasApp ? project.android.applicationVariants : project.android.libraryVariants


        tinyinfo = project.extensions.create("tinyinfo", TinyInfo);
        project.afterEvaluate {
            variants.all { variant ->


                if (tinyinfo.skip == true) {
                    printlog "skip tinyPicPlugin Task!!!"

                    return
                }

                def processResourceTask = project.tasks.findByName("process${variant.name.capitalize()}Resources")
                def tinyPicPlugin = "tinyPicPlugin${variant.name.capitalize()}"
                project.task(tinyPicPlugin) << {

                    def apiKey = tinyinfo.apiKey
                    printlog "tiny apiKey:" + apiKey

//                    def maxNum = tinyinfo.maxNum

//                    println "tiny maxNum:" + maxNum
//                    if (maxNum == null) {
//                        println "maxNum==null"
//                    }
                    try {
                        Tinify.setKey("${apiKey}");
                        Tinify.validate();
                    } catch (java.lang.Exception e) {
                        // Validation of API key failed.
                        throw new Exception("Tiny Validation of API key failed.")
                    }

                    def n = 0
                    def thisTimeTotalSaveSize = 0
                    def lastAllTimesTotalSaveSize = 0
                    def compressedListFile = new File("${project.projectDir}/tinypic_compressed_list.txt")
                    if (!compressedListFile.exists()) {
                        compressedListFile.createNewFile()
                    }
                    def whiteListFile = new File("${project.projectDir}/tinypic_white_list.txt")
                    if (!whiteListFile.exists()) {
                        whiteListFile.createNewFile()
                    }
                    def ln = System.getProperty('line.separator')

                    def compressedList = new ArrayList()
                    compressedListFile.eachLine() {
                        compressedList.add(it)
                        printlog "compressedList line:" + it

                    }



                    String resPath = "${project.projectDir}/src/main/res/"
                    def dir = new File("${resPath}")
                    dir.eachDirMatch(~/drawable[a-z-]*/) { drawDir ->
                        def file = new File("${drawDir}")
                        file.eachFile { filePathAndName ->
                            def fileName = filePathAndName.name

                            def isInWhiteList = false
                            def isInCompressedList = false
                            whiteListFile.eachLine { whiteName ->
                                if (fileName.equals(whiteName)) {
                                    isInWhiteList = true
                                }
                            }

                            compressedList.each {
                                if (fileName.equals(it)) {
                                    isInCompressedList = true
                                }
                                String str = it.toString()
                                if (str.contains("all times totalSaveSize")) {
                                    lastAllTimesTotalSaveSize = it.toString().subSequence(str.indexOf(":") + 1, str.indexOf("B"))
                                }

                            }


                            if (!isInWhiteList && !isInCompressedList) {
                                if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
                                    if (!fileName.contains(".9")) {
//                                        if (maxNum != null) {
//                                            if (n < maxNum) {
//                                                n++
//                                            } else {
//                                                return
//                                            }
//                                        }

                                        printlog "find target pic >>>>>>>>>>>>>" + fileName

                                        def picName = fileName.split('\\.')[0]
                                        def suffix = fileName.split('\\.')[1]
//                                        printlog "picName:" + picName

                                        def targetFile = new File("${filePathAndName}")
                                        def fis = new FileInputStream(targetFile);

                                        try {
                                            def beforeSize = fis.available();
                                            if (beforeSize < tinyinfo.minSize) {
                                                printlog picName + " is too small," + "it's size is:" + beforeSize + "B"
                                                return
                                            }
                                            printlog "beforeSize:" + beforeSize + "B"

                                            // Use the Tinify API client
                                            def tSource = Tinify.fromFile("${filePathAndName}");
                                            tSource.toFile("${filePathAndName}");

                                            def afterSize = fis.available();
                                            printlog "afterSize:" + afterSize + "B"


                                            def saveSize = beforeSize - afterSize
                                            printlog("saveSize:" + saveSize + "B")

                                            thisTimeTotalSaveSize += saveSize

                                            compressedListFile << "${fileName}${ln}"

                                        } catch (AccountException e) {
                                            System.out.println("The error message is: " + e.getMessage());
                                            // Verify your API key and account limit.
                                        } catch (ClientException e) {
                                            // Check your source image and request options.

                                            System.out.println("The error message is: " + e.getMessage());
                                        } catch (ServerException e) {
                                            // Temporary issue with the Tinify API.
                                            System.out.println("The error message is: " + e.getMessage());
                                        } catch (ConnectionException e) {
                                            // A network connection error occurred.
                                            System.out.println("The error message is: " + e.getMessage());
                                        } catch (java.lang.Exception e) {
                                            // Something else went wrong, unrelated to the Tinify API.
                                            System.out.println("The error message is: " + e.getMessage());
                                        }

                                    }
                                }
                            }

                        }
                    }

                    if (thisTimeTotalSaveSize.toInteger() == 0) {
                        printlog "no need save"

                        return
                    }
                    def allTimesTotalSaveSize = thisTimeTotalSaveSize.toInteger() + lastAllTimesTotalSaveSize.toInteger()
                    printlog "totalSaveSize>>>>>>>>>>>>>>>>>>>>>>" + thisTimeTotalSaveSize + "B"

                    compressedListFile << ">>>>>>>>>lastAllTimesTotalSaveSize>>>>>>>>>>:${lastAllTimesTotalSaveSize}" + "B" + " ${ln}"
                    compressedListFile << ">>>>>>>>>>>>>>this time totalSaveSize>>>>>>>>>>>>>>>:${thisTimeTotalSaveSize}" + "B" + " ${ln}"
                    compressedListFile << ">>>>>>>>>>>>>>>>>>>all times totalSaveSize>>>>>>>>>>>>>>>>>>>>:${allTimesTotalSaveSize}" + "B" + " ${ln}"


                }

                //将自定义task放在dx task之前执行
                project.tasks.findByName(tinyPicPlugin).dependsOn processResourceTask.taskDependencies.getDependencies(processResourceTask)
                processResourceTask.dependsOn project.tasks.findByName(tinyPicPlugin)
            }

        }

    }

    void printlog(String msg) {
        if (tinyinfo.isShowLog == true) {
            println msg
        }
    }
}


class TinyInfo {
    String apiKey
    String maxNum

    int minSize
    boolean skip
    boolean isShowLog
}