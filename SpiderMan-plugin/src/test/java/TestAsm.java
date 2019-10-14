import java.io.IOException;
import java.io.PrintWriter;
import org.junit.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;

public class TestAsm {

    @Test
    public void testAsm() throws IOException {
        //reader
//        ClassReader classReader = new ClassReader("java.lang.String");//className
        ClassReader classReader = new ClassReader("vip.ruoyun.java.TestJava");//className
        //writer
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);

        //打印输出，查看结果
        PrintWriter printWriter = new PrintWriter("./src/test/java/testAsm.text");//
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(classWriter, printWriter);//用来输出生成的代码 text

        //visitor
        AsmClassVisitor asmClassVisitor = new AsmClassVisitor(traceClassVisitor);
        //转换
        classReader.accept(asmClassVisitor, ClassReader.EXPAND_FRAMES);
        //输出字节码
        final byte[] bytes = classWriter.toByteArray();
        traceClassVisitor.p.print(printWriter);//输出代码
    }
    //=====---------- visitMethod ----------=====
    //methodName: <init>
    //methodDes: ()V
    //=====---------- visitMethod ----------=====
    //methodName: registerNatives
    //methodDes: ()V
    //=====---------- visitMethod ----------=====
    //methodName: getClass
    //methodDes: ()Ljava/lang/Class;
    //=====---------- visitMethod ----------=====
    //methodName: hashCode
    //methodDes: ()I
    //=====---------- visitMethod ----------=====
    //methodName: equals
    //methodDes: (Ljava/lang/Object;)Z
    //=====---------- visitMethod ----------=====
    //methodName: clone
    //methodDes: ()Ljava/lang/Object;
    //=====---------- visitMethod ----------=====
    //methodName: toString
    //methodDes: ()Ljava/lang/String;
    //=====---------- visitMethod ----------=====
    //methodName: notify
    //methodDes: ()V
    //=====---------- visitMethod ----------=====
    //methodName: notifyAll
    //methodDes: ()V
    //=====---------- visitMethod ----------=====
    //methodName: wait
    //methodDes: (J)V
    //=====---------- visitMethod ----------=====
    //methodName: wait
    //methodDes: (JI)V
    //=====---------- visitMethod ----------=====
    //methodName: wait
    //methodDes: ()V
    //=====---------- visitMethod ----------=====
    //methodName: finalize
    //methodDes: ()V
    //=====---------- visitMethod ----------=====
    //methodName: <clinit>
    //methodDes: ()V

}
