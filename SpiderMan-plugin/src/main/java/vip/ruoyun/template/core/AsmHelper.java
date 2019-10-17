package vip.ruoyun.template.core;

import java.io.IOException;
import java.io.InputStream;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import vip.ruoyun.template.asm.AsmClassVisitor;

class AsmHelper {

    static byte[] readSingleClassToByteArray(InputStream inputStream) throws IOException {
        //开始处理，通过 ASM
        ClassReader classReader = new ClassReader(inputStream);
        //writer
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        //visitor
        AsmClassVisitor asmClassVisitor = new AsmClassVisitor(classWriter);
        //转换
        classReader.accept(asmClassVisitor, ClassReader.EXPAND_FRAMES);
        //输出字节码
        return classWriter.toByteArray();
    }

    static boolean canReadableClass(String fullQualifiedClassName) {
        return fullQualifiedClassName.endsWith(".class") && !fullQualifiedClassName.contains("R$")
                && !fullQualifiedClassName.contains("R.class") && !fullQualifiedClassName
                .contains("BuildConfig.class");
    }
}
