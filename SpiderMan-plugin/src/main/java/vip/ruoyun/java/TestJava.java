package vip.ruoyun.java;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@TestAnnotation("类")
public class TestJava {

    @TestAnnotation("方法")
    public void test(String string) {

//        System.out.println(string);

        String name = "测试";
//        System.out.println(string + name);

        //栈
        //this 第一个变量
        //string 第二个变量
        //name 第三个变量
        print(string, name);

        //栈
        //this 第一个变量
        //name 第二个变量
        print(name);

        try {
            final Method test = getClass().getDeclaredMethod("test");
            final TestAnnotation annotation = test.getAnnotation(TestAnnotation.class);
            annotation.value();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void print(String s1, String s2) {

    }

    //methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
    //methodVisitor.visitLdcInsn("123");
    //methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    public void print(String s2) {
        System.out.println("123");
    }

    //// access flags 0x1
    //  public test(Ljava/lang/String;)V
    //  @Lvip/ruoyun/java/TestAnnotation;(value="\u65b9\u6cd5")
    //   L0
    //    LINENUMBER 9 L0
    //    RETURN
    //   L1
    //    LOCALVARIABLE this Lvip/ruoyun/java/TestJava; L0 L1 0
    //    LOCALVARIABLE string Ljava/lang/String; L0 L1 1
    //    MAXSTACK = 0
    //    MAXLOCALS = 2

    //// access flags 0x1
    //  public test(Ljava/lang/String;)V
    //  @Lvip/ruoyun/java/TestAnnotation;(value="\u65b9\u6cd5")
    //   L0
    //    LINENUMBER 8 L0
    //    GETSTATIC java/lang/System.out : Ljava/io/PrintStream;
    //    ALOAD 1
    //    INVOKEVIRTUAL java/io/PrintStream.println (Ljava/lang/String;)V
    //   L1
    //    LINENUMBER 9 L1
    //    RETURN
    //   L2
    //    LOCALVARIABLE this Lvip/ruoyun/java/TestJava; L0 L2 0
    //    LOCALVARIABLE string Ljava/lang/String; L0 L2 1
    //    MAXSTACK = 2
    //    MAXLOCALS = 2
}
