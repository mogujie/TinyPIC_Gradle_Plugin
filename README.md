#  TinyPIC Gradle Plugin #


*Read this in other languages: [简体中文](README.zh-cn.md).*

## UpdateInfo
### 1.1.4
    add two new properties:
    //if skpi the task
    skip = true/false 
    //if print log 
    isShowLog = true/false
### 1.1.3.1
    rename packagename ,delete redundant code
### 1.1.3
    add new logic: if the save size is 0B,won't print message into tiny_compressed_list.txt
### 1.1.2
    add new statistics: all times total save size

***

Dear,Do you still tiny your pic manually?
tinyPIC is a gradle plugin which can tiny batch pics auto in your res directory. 
### Usage of tinyPIC:
***

add below code in the outer build.gradle file(which is in the same directory of settings.gradle)

    classpath 'com.mogujie.gradle:tinyPicPlugin:1.1.4'
    
add below code in the inner build.gradle file(which is in the same directory of src)

 	apply plugin: 'tinyPIC'

 	tinyinfo {
                apiKey = 'your tiny API key'
                //if skpi the task
                skip = true/false 
                //if print log 
                isShowLog = true/false
    }
 	
### Get tiny API key
***

browse 	https://tinypng.com/developers  and provide an email
Tiny will give you an email with a link ,open the link you will see your API KEY on the left ,and the amount of you this month
tinyAPI will give every key 500 free limited to compressed pic success.

tinyPIC have a tinypic_compressed_list.txt file to record the pic we successed compressed,
next time tinyPIC will pass the pic to save your amount.
tinyPIC also have a tinypic_white_list.txt file,if the pic doesn't look good after tiny ,you can revert it and add it's name in the file ,
so next time tinyPIC will ignore it.
OK,have a try and have fun!!!



## Licence
***
TinyPIC is licensed under the MIT license



### upload to bintray
***
1、write into local.properties    
bintray.user=xxxxxxx     
bintray.apikey=xxxxxxxxxx  
2、gradlew install  
3、gradlew bintrayUpload

