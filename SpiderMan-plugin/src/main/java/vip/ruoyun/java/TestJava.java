package vip.ruoyun.java;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@TestAnnotation("类")
public class TestJava {

    @TestAnnotation("方法")
    public void test(String string) {
//        System.out.println(string);

//        try {
//            final Method test = getClass().getDeclaredMethod("test");
//            final TestAnnotation annotation = test.getAnnotation(TestAnnotation.class);
//            annotation.value();
//
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
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
