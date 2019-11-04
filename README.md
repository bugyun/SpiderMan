# SpiderMan

SpiderMan 是一个在编译时期动态修改代码的工具。

支持增量、并发编译。支持 ASM、Javassist。

包括以下功能：
- 全埋点 AutoTracker
- 日志，通过注解在任何方法中打印
- tryCatch，通过注解在任何方法中加入tryCatch
- 等等，其他功能在开发中


## 自定义插件

可以基于 SpiderMan-plugin-core 来进行快速开发

[点我，链接](https://github.com/bugyun/SpiderMan/tree/master/SpiderMan-plugin-core)


## AutoTracker 全埋点

Android字节码插件，编译期间动态修改代码，基于 SpiderMan-plugin-core 插件开发的全埋点工具

[点我，链接](https://github.com/bugyun/SpiderMan/tree/master/SpiderMan-tracker-core)



