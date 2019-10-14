import static org.objectweb.asm.Opcodes.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import org.junit.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

public class TestClassWriter {

    //package pkg;
    //public interface Comparable extends Mesurable {
    //int LESS = -1;
    //int EQUAL = 0;
    //int GREATER = 1;
    //int compareTo(Object o);
    //}
    @Test
    public void gen() throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter("./src/test/java/testclass.text");//

        //flags
        //new ClassWriter(0) 将不会自动进行计算。你必须自己计算帧、局部变量和操作数栈的大小。
        //new ClassWriter(ClassWriter.COMPUTE_MAXS) 局部变量和操作数栈的大小就会自动计算。但是，你仍然需要自己调用 visitMaxs 方法，尽管你可以使用任何参数： 实际上这些参数会被忽略，然后重新计算。使用这个选项，你仍然需要计算帧的大小。
        //new ClassWriter（ClassWriter.COMPUTE_FRAMES） 所有的大小都将自动为你计算。 你也不许要调用 visitFrame 方法，但是你仍然需要调用 visitMaxs 方法（参数将被忽 略然后重新计算）。
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(cw, printWriter);//用来输出生成的代码 text
        //用来检测生成的代码是否正确,如果出错就会抛出异常 IllegalStateException 或者 IllegalArgumentException
        CheckClassAdapter checkClassAdapter = new CheckClassAdapter(traceClassVisitor);
        checkClassAdapter.visit(V1_7, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
                "pkg/Comparable", null, "java/lang/Object",
                new String[]{"pkg/Mesurable"});

        checkClassAdapter.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "LESS", "I",
                null, -1).visitEnd();

        checkClassAdapter.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "EQUAL", "I",
                null, 0).visitEnd();

        checkClassAdapter.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "GREATER", "I",
                null, 1).visitEnd();

        checkClassAdapter.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "compareTo",
                "(Ljava/lang/Object;)I", null, null).visitEnd();
        checkClassAdapter.visitEnd();

//        byte[] b = cw.toByteArray();
        traceClassVisitor.p.print(printWriter);//输出代码
        final List<Object> text = traceClassVisitor.p.getText();
        for (Object objects : text) {
            System.out.println(objects);
        }
    }


    @Test
    public void testASMifierClassVisitor() {
//        ASMifierClassVisitor as

    }

}
