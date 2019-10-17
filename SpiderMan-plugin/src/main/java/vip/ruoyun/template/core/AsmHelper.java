package vip.ruoyun.template.core;

import java.io.IOException;
import java.io.InputStream;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import vip.ruoyun.template.asm.AsmClassVisitor;

/**
 * 处理 ASM 的帮助类
 */
class AsmHelper {

    /**
     * ASM 的入口
     */
    static byte[] readSingleClassToByteArray(InputStream inputStream) throws IOException {
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
    static boolean canReadableClass(String fullQualifiedClassName) {
        return fullQualifiedClassName.endsWith(".class")//后缀为.class 的文件
                && !fullQualifiedClassName.contains("R$") //不包含 R$ 文件
                && !fullQualifiedClassName.contains("R.class")//不包含 R.class 文件
                && !fullQualifiedClassName.contains("BuildConfig.class");//不包含 BuildConfig.class 文件
    }
}
