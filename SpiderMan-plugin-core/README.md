# SpiderMan-plugin-core

Android Gradle Transform 加速开发的工具

开发者完全不用关心 Transform 的各种复杂的文件操作和线程间的切换问题。

优点：
1. 优化文件处理速度
2. 启用多线程来优化速度
3. 比市面上相关插件速度快1倍以上
4. 避免处理各种文件
5. 只需要继承几个类就可以完成 ASM/Javassist 的代码注入
6. 支持 ASM 和 Javassist 

## 入门

关于插件开发的流程和如何上传到 jcenter 仓库，请参考下面的链接~

[Android Gradle 自定义入门](https://ruoyun.vip/post/android-gradle-%E8%87%AA%E5%AE%9A%E4%B9%89%E5%85%A5%E9%97%A8/)

## 依赖
在你写 gradle 插件的过程中，在plugin 插件的 build.gradle 中添加下面的依赖。

```groovy
implementation 'vip.ruoyun.spiderman.plugin:spider-man-core:1.0.1'
```

## 使用

### 自定义 Plugin
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

### 自定义 Transform

自定义 Transform，继承 SpiderManTransform 类，然后实现如下方法：
- isOpen() 是否打开 class 转换
- getClassReader() 新建自己的 ClassReader
- beginTransform() Transform 开始之前会执行此方法，可以在此函数中进行日志的输出，或者时间的计算
- endTransform() Transform 结束之前会执行此方法，可以在此函数中进行日志的输出，或者时间的计算


```java
public class AutoTrackerTransform extends SpiderManTransform {

    private AutoTrackerExt mAutoTrackerExt;

    public AutoTrackerTransform(final Project project) {
        super(project);
        mAutoTrackerExt = project.getExtensions().getByType(AutoTrackerExt.class);
    }

    //Transform 开始之前会执行此方法，可以在此函数中进行日志的输出，或者时间的计算
    @Override
    public void beginTransform() {

    }
    //Transform 结束之前会执行此方法，可以在此函数中进行日志的输出，或者时间的计算
    @Override
    public void endTransform() {

    }
    
    //是否打开
    @Override
    public boolean isOpen() {
        return mAutoTrackerExt.isOpen();
    }

    //创建自定义的 classReader
    @Override
    public IClassReader getClassReader() {
        return new TestIAsmReader();
    }
}
```

### 自定义 IClassReader
可以实现 SpiderManAsmReader 类，或者直接实现 IClassReader 接口，但是后者需要重写 2 个方法。

#### 实现 SpiderManAsmReader 类
```java
public class TestIAsmReader extends SpiderManAsmReader {

    @Override
    public ClassVisitor createClassVisitor(final ClassWriter classWriter) {
        return new ClassVisitor(Opcodes.ASM7, classWriter) {
            @Override
            public void visit(final int version, final int access, final String name, final String signature,
                    final String superName,
                    final String[] interfaces) {
                super.visit(version, access, name, signature, superName, interfaces);
            }
        };
    }
}
```

#### 进阶使用,实现 IClassReader 接口

```java
public interface IClassReader {

    /**
     * 读取 class 文件，在此处可以直接使用 ASM 来对文件进行处理
     */
    byte[] readSingleClassToByteArray(InputStream inputStream) throws Exception;

    /**
     * 是否需要 ASM 文件扫描
     */
    boolean canReadableClass(String fullQualifiedClassName);

}
```

```java
class AsmClassReader implements IClassReader {

    /**
     * ASM 的入口
     */
    public byte[] readSingleClassToByteArray(InputStream inputStream) throws Exception {
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

## Javassist 支持
如果你想使用 Javassist 来对代码进行修改，那么可以实现 IAsmReader 接口，然后实现方法。

通过 classPool 来读取具体的流，然后找到具体的类。就可以完美使用 Javassist 来处理 class。

添加依赖
```groovy
implementation 'org.javassist:javassist:3.26.0-GA'
```

具体代码如下：
```java
public class JavassistReader implements IClassReader {

    /**
     * Javassist 参考资料
     * https://www.cnblogs.com/chiangchou/p/javassist.html
     */
    @Override
    public byte[] readSingleClassToByteArray(final InputStream inputStream) throws Exception {
        ClassPool classPool = new ClassPool(true);// 创建新的 ClassPool，避免内存溢出
        //使用 classPool 加载类
        //classPool.insertClassPath(new ClassClassPath(this.getClass()));
        CtClass ctClass = classPool.makeClass(inputStream);//通过 InputStream 的方式，加载这个类

        // 去除接口、注解、枚举、原生、数组等类型的类，以及代理类不解析
        if (ctClass.isInterface() || ctClass.isAnnotation() || ctClass.isEnum() || ctClass.isPrimitive() || ctClass
                .isArray() || ctClass.getSimpleName().contains("$")) {
            return IOUtils.toByteArray(inputStream);//如果不解析，就直接返回这流
        }
        // 获取所有声明的方法
        CtMethod[] methods = ctClass.getDeclaredMethods();
        for (CtMethod method : methods) {
            // 代理方法不解析
            if (method.getName().contains("$")) {
                continue;
            }
            // 包名
            String packageName = ctClass.getPackageName();
            // 类名
            String className = ctClass.getSimpleName();
            // 方法名
            String methodName = method.getName();
            // 参数：method.getLongName() 返回格式：com.test.TestService.selectOrder(java.lang.String,java.util.List,com.test.Order)，所以截取括号中的即可
            String methodSignature = StringUtils
                    .defaultIfBlank(StringUtils.substringBetween(method.getLongName(), "(", ")"), null);
        }
        return ctClass.toBytecode();
    }

    @Override
    public boolean canReadableClass(final String fullQualifiedClassName) {
        return fullQualifiedClassName.endsWith(".class")//后缀为.class 的文件
                && !fullQualifiedClassName.startsWith("androidx.")//androidx 的包不读取
                && !fullQualifiedClassName.startsWith("android.")//android 的包不读取
                && !fullQualifiedClassName.contains("R$") //不包含 R$ 文件
                && !fullQualifiedClassName.contains("R.class")//不包含 R.class 文件
                && !fullQualifiedClassName.contains("BuildConfig.class");//不包含 BuildConfig.class 文件
    }
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

