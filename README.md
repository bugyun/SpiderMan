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
首先定义自己的 Plugin,比如自定义一个打点的 Plugin,然后在里面注册自己的 Transform

```java
public class AutoTrackerPlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        if (!project.getPlugins().hasPlugin("com.android.application")) {
            throw new GradleException("TrackerPlugin: Android Application plugin required");
        }
        project.getExtensions().create("autoTracker", AutoTrackerExt.class);
        AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
        appExtension.registerTransform(new AutoTrackerTransform(project), Collections.EMPTY_LIST);
    }
}
```


自定义 Transform，继承 SpiderManTransform 类，然后实现如下方法：
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

例子
```java
class AsmHelper implements IAsmReader {

    /**
     * ASM 的入口
     */
    public byte[] readSingleClassToByteArray(InputStream inputStream) throws IOException {
        //开始处理，通过 ASM
        ClassReader classReader = new ClassReader(inputStream);
        //writer
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        //visitor
        AsmClassVisitor asmClassVisitor = new AsmClassVisitor(classWriter);
        //转换
        classReader.accept(asmClassVisitor, ClassReader.EXPAND_FRAMES);//didi 开源 parsingOptions 是 0
        //输出字节码
        return classWriter.toByteArray();
    }

    /**
     * asm 是否需要访问内部
     */
    public boolean canReadableClass(String fullQualifiedClassName) {
        return fullQualifiedClassName.endsWith(".class")//后缀为.class 的文件
                && !fullQualifiedClassName.startsWith("androidx.")//androidx 的包不读取
                && !fullQualifiedClassName.startsWith("android.")//android 的包不读取
                && !fullQualifiedClassName.contains("R$") //不包含 R$ 文件
                && !fullQualifiedClassName.contains("R.class")//不包含 R.class 文件
                && !fullQualifiedClassName.contains("BuildConfig.class");//不包含 BuildConfig.class 文件
    }
}
```

然后编写自己的插件，按着插件的发布流程，就可以发布到 jcenter 仓库中，就可以直接使用了~
关于插件开发的流程和如果上传到 jcenter 仓库，请参考下面的链接~

[Android Gradle 自定义入门](https://ruoyun.vip/post/android-gradle-%E8%87%AA%E5%AE%9A%E4%B9%89%E5%85%A5%E9%97%A8/)


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

