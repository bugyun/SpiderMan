package vip.ruoyun.template.asm;

import java.util.Arrays;
import java.util.HashSet;

import java.util.Map.Entry;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import vip.ruoyun.template.asm.config.FragmentConfig;
import vip.ruoyun.template.asm.config.MethodCell;
import vip.ruoyun.template.utils.LogM;

public class AsmClassVisitor extends ClassVisitor implements Opcodes {

    private HashSet<String> visitedFragmentMethods = new HashSet<>();

    private String annotationValue = "";

    private String superName;

    private boolean isFragmentVisitor = false;

    public AsmClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM7, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        LogM.log("=====---------- visitBegin ----------=====");
        LogM.log("version:" + version);
        LogM.log("access：" + access);
        LogM.log("name:" + name);
        LogM.log("signature:" + signature);
        LogM.log("superName:" + superName);
        LogM.log("interfaces:" + Arrays.toString(interfaces));
        this.superName = superName;
        if (FragmentConfig.supportFragmentClassList.contains(superName)) {
            isFragmentVisitor = true;
        }
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        LogM.log("=====---------- visitAnnotation ----------=====");
        LogM.log(desc);
        return new ClassAnnotationVisitor(api, super.visitAnnotation(desc, visible));
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        LogM.log("=====---------- visitMethod ----------=====");
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (isFragmentVisitor) {
            return new FragmentMethodVisitor(api, mv, access, name, desc, superName, visitedFragmentMethods);
        } else {
            return new ViewMethodVisitor(api, mv, access, name, desc);
        }
        //return super.visitMethod(access, name, desc, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        LogM.log("=====---------- visitEnd ----------=====");
        if (isFragmentVisitor) {
            for (final Entry<String, MethodCell> next : FragmentConfig.sFragmentMethods.entrySet()) {
                LogM.hint("=====---------- test...{} , {}----------=====", next.getKey(), next.getValue());
                if (!visitedFragmentMethods.contains(next.getKey())) {
                    FragmentConfig.fragmentMethodAndSuper(next.getValue(), cv, superName);
                }
            }
        }
    }

    //annotationVisitor0 = methodVisitor.visitAnnotation("Lvip/ruoyun/java/TestAnnotation;", true);
    //                annotationVisitor0.visit("value", "\u65b9\u6cd5");
    //                annotationVisitor0.visitEnd();
    //类上的注解
    public class ClassAnnotationVisitor extends AnnotationVisitor {

        ClassAnnotationVisitor(final int api, final AnnotationVisitor annotationVisitor) {
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
