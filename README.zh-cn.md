#  TinyPIC Gradle Plugin #


*其他语言版本: [English](README.md).*

## 更新信息
### 3.0.0
    支持大小过滤
### 1.1.4
    添加了新配置属性:
    //是否跳过此task
    skip = true/false 
    //是否打印日志
    isShowLog = true/false

### 1.1.3.1
    规范了目录结构，删除了冗余代码
### 1.1.3
    添加如果本次压缩是0B  就不输出信息到文件里面去
### 1.1.2
    添加总减少大小统计

***

各位亲们，你们还在手动添加一个个图片到tiny上压缩，下载吗？

tinyPIC是一个批量tiny处理res图片的gradle插件，以后您尽管往res里放切好的图，tiny处理的工作就交给他好了。

### tinyPIC插件的使用方法如下：
***
在外层的build.gradle文件中（即与settings.gradle同级的文件）添加如下代码

 	    classpath 'com.qinuo.gradle:tinyPicPlugin:3.0.0'
 	
在内层build.gradle文件中（即与src同级的文件）添加如下代码



 	    apply plugin: 'tinyPIC'
    
        tinyinfo {
            apiKey = '你申请的tiny API key'
            //是否跳过此task
            skip = true/false 
            //设置大小
            minSize = 10*1024
            //是否打印日志
            isShowLog = true/false
        }



### tiny API key申请的方法：
***

进入	https://tinypng.com/developers 
找个邮箱申请就可以了



通过邮箱返回的链接，你就能进入你的账号的信息页了，
左边是你的 API KEY 
右边是你的账号每个月压缩图片的使用额度


tinyAPI 每个月给每个账号500张的免费压缩额度，我在我的tinyPIC里添加了一个tinypic_compressed_list.txt 文件
该文件记录了已经被我们成功压缩过的图片，如图：
PastedGraphic-6.tiff

这样在以后build的时候就不会再使用API额度对它进行处理了，对于每个业务组来说每月500完全够用了。不够用怎么办？再换个邮箱注册啊，亲


tinyPIC还会生成一个tinypic_white_list.txt  它提供了白名单功能，如果发现经过tiny处理的图片UI效果不佳，可以通过添加文件名到此文件下进行忽略tiny压缩处理

好了，快点从日常手动压缩业务图片中解脱出来吧



## 协议
***
MGJTempStore 被许可在 MIT 协议下使用。查阅 LICENSE 文件来获得更多信息。