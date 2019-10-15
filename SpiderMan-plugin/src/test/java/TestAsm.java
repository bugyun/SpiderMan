import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

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


    //栅栏的方式,先执行 1 2 ,然后阻塞,然后才开始执行 3 4
    @Test
    public void testC() throws InterruptedException {
        ForkJoinPool executor = new ForkJoinPool(Runtime.getRuntime().availableProcessors(),
                ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
        executor.execute(() -> {
            System.out.println("1 begin");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("1 end");
        });

        executor.execute(() -> {
            System.out.println("2 begin");

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("2 end");
        });
        executor.awaitQuiescence(1, TimeUnit.MINUTES);
        System.out.println("开始");

        executor.execute(() -> {
            System.out.println("3 begin");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("3 end");
        });

        executor.execute(() -> {
            System.out.println("4 begin");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("4 end");
        });
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("结束");

    }
}
