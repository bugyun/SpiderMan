# SpiderMan
Android Gradle Transform 加速开发的工具

开发者完全不用关心 Transform 的各种复杂的文件操作和线程间的切换问题。

优点：
1. 优化文件处理速度
2. 启用多线程来优化速度
3. 比市面上相关插件速度快1倍以上
4. 避免处理各种文件
5. 只需要继承几个类就可以完成 ASM 的代码注入

## 依赖
在你写 gradle 插件的过程中，在plugin 插件的 build.gradle 中添加下面的依赖。

```groovy
implementation 'vip.ruoyun.spiderman.plugin:spider-man-core:1.0.0'
```

## 使用
比如写一个打点的 plugin ，继承 SpiderManTransform 类，然后实现如下方法：
- isOpen() 是否打开 ASM 转换
- getAsmReader() 新建自己的 AsmReader


```java
public class AutoTrackerTransform extends SpiderManTransform {

    private AutoTrackerExt mAutoTrackerExt;

    public AutoTrackerTransform(final Project project) {
        super(project);
        mAutoTrackerExt = project.getExtensions().getByType(AutoTrackerExt.class);
    }

    @Override
    public boolean isOpen() {
        return mAutoTrackerExt.isOpen();
    }

    @Override
    public IAsmReader getAsmReader() {
        return new AsmHelper();
    }
}
```

然后实现 IAsmReader 接口
```java
public interface IAsmReader {

    /**
     * 读取 class 文件，在此处可以直接使用 ASM 来对文件进行处理
     */
    byte[] readSingleClassToByteArray(InputStream inputStream) throws IOException;

    /**
     * 是否需要 ASM 文件扫描
     */
    boolean canReadableClass(String fullQualifiedClassName);

}
```

## 日志
可以通过 LogM 来输出日志

log:输出默认颜色日志
```java
LogM.isLog = true;//设置是否打印日志
LogM.log("日志");
LogM.log("日志{}","第一");
```

hint:输出红色日志
```java
LogM.isHint = true;//设置是否打印红色日志
LogM.hint("日志");
LogM.hint("日志{}","第一");
```

## AsmUtils 帮助类

```java
AsmUtils.isSynthetic(int access);//方法类型是否是 Synthetic
AsmUtils.isPrivate(int access);//方法类型是否是 Private
AsmUtils.isPublic(int access);//方法类型是否是 Public
AsmUtils.isStatic(int access);//方法类型是否是 Static
AsmUtils.isLambda(int access);//方法类型是否是 Lambda
```


## 注意

如果是 设置 open 开关，那么请 clean project ,才会生效。不然项目会有问题。

