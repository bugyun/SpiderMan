package vip.ruoyun.spiderman.track.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;
import vip.ruoyun.spiderman.track.asm.config.ViewConfig;
import vip.ruoyun.template.utils.AsmUtils;
import vip.ruoyun.template.utils.LogM;

public class ViewMethodVisitor extends AdviceAdapter {

    private String annotationValue = "";

    ViewMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc) {
        super(api, mv, access, name, desc);
        LogM.log("methodName: " + getName());
        LogM.log("methodDes: " + methodDesc);
    }


    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        LogM.log("visitAnnotation：begin");
        LogM.log(desc + visible);
        return new MethodAnnotationVisitor(api, super.visitAnnotation(desc, visible));
    }


    //    GETSTATIC java/lang/System.out : Ljava/io/PrintStream;
    //    ALOAD 1
    //    INVOKEVIRTUAL java/io/PrintStream.println (Ljava/lang/String;)V
    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        LogM.log("onMethodEnter");
        if (getName().startsWith("lambda$") && ViewConfig.sViewLambdaMethods.containsKey(methodDesc) && AsmUtils
                .isLambda(methodAccess)) { //lambda 方法
            ViewConfig.lambdaMethod(mv);
        }

        if (!(AsmUtils.isPublic(methodAccess) && !AsmUtils.isStatic(methodAccess))) {
            return;//如果不是 public 非 static 的方法，直接 return
        }

        if (ViewConfig.sViewMethods.containsKey(getName() + methodDesc)) {
            ViewConfig.method(mv);
        }
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
    }

    //annotationVisitor0 = methodVisitor.visitAnnotation("Lvip/ruoyun/java/TestAnnotation;", true);
    //                annotationVisitor0.visit("value", "\u65b9\u6cd5");
    //                annotationVisitor0.visitEnd();
    private class MethodAnnotationVisitor extends AnnotationVisitor {

        MethodAnnotationVisitor(final int api, final AnnotationVisitor annotationVisitor) {
            super(api, annotationVisitor);
        }

        @Override
        public void visit(final String name, final Object value) {
            super.visit(name, value);
            LogM.log("name: " + name);
            LogM.log("value: " + value);
            if (value != null) {
                annotationValue = value.toString();
            }
        }

        @Override
        public void visitEnd() {
            super.visitEnd();
            LogM.log("visitAnnotation：end");
        }
    }

}
