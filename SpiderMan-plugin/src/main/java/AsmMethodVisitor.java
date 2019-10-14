import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

public class AsmMethodVisitor extends AdviceAdapter {

    private String methodName;

    private String methodDes;

    public AsmMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc) {
        super(api, mv, access, name, desc);
        methodName = name;
        methodDes = desc;
        LogM.log("methodName: " + methodName);
        LogM.log("methodDes: " + methodDes);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        LogM.log("visitAnnotation：begin");
        LogM.log(desc + visible);
        return new MyAnnotationVisitor(api, super.visitAnnotation(desc, visible));
    }


    //    GETSTATIC java/lang/System.out : Ljava/io/PrintStream;
    //    ALOAD 1
    //    INVOKEVIRTUAL java/io/PrintStream.println (Ljava/lang/String;)V
    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        LogM.log("onMethodEnter");
        //当方法进去的时候，判断这个方法是否
        if ("test".equals(methodName) && "(Ljava/lang/String;)V".equals(methodDes)) {
            LogM.log("test 方法begin");
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            LogM.log("test 方法end");
        }
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
        LogM.log("onMethodExit:" + opcode);
    }

    //annotationVisitor0 = methodVisitor.visitAnnotation("Lvip/ruoyun/java/TestAnnotation;", true);
    //                annotationVisitor0.visit("value", "\u65b9\u6cd5");
    //                annotationVisitor0.visitEnd();
    public static class MyAnnotationVisitor extends AnnotationVisitor {

        public MyAnnotationVisitor(final int api) {
            super(api);
        }

        public MyAnnotationVisitor(final int api, final AnnotationVisitor annotationVisitor) {
            super(api, annotationVisitor);
        }

        @Override
        public void visit(final String name, final Object value) {
            super.visit(name, value);
            LogM.log("name: " + name);
            LogM.log("value: " + value);
        }

        @Override
        public void visitEnd() {
            super.visitEnd();
            LogM.log("visitAnnotation：end");
        }
    }

}
