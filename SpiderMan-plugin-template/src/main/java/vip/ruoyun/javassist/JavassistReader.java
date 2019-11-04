package vip.ruoyun.javassist;

import java.io.IOException;
import java.io.InputStream;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.apache.commons.io.IOUtils;
import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils;
import vip.ruoyun.plugin.core.IClassReader;

public class JavassistReader implements IClassReader {

    /**
     * Javassist 参考资料
     * https://www.cnblogs.com/chiangchou/p/javassist.html
     */
    @Override
    public byte[] readSingleClassToByteArray(final InputStream inputStream) throws IOException {
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
//            method.addCatch();
        }
        try {
            return ctClass.toBytecode();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return IOUtils.toByteArray(inputStream);
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
