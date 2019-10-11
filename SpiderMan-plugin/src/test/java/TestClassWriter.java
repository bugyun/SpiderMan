import static org.objectweb.asm.Opcodes.*;

import java.util.Arrays;
import org.junit.*;
import org.objectweb.asm.ClassWriter;

public class TestClassWriter {

    //package pkg;
    //public interface Comparable extends Mesurable {
    //int LESS = -1;
    //int EQUAL = 0;
    //int GREATER = 1;
    //int compareTo(Object o);
    //}
    @Test
    public void gen() {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_7, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
                "pkg/Comparable", null, "java/lang/Object",
                new String[]{"pkg/Mesurable"});

        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "LESS", "I",
                null, -1).visitEnd();

        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "EQUAL", "I",
                null, 0).visitEnd();

        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "GREATER", "I",
                null, 1).visitEnd();

        cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "compareTo",
                "(Ljava/lang/Object;)I", null, null).visitEnd();
        cw.visitEnd();
        byte[] b = cw.toByteArray();
        System.out.println(Arrays.toString(cw.toByteArray()));
    }

}
