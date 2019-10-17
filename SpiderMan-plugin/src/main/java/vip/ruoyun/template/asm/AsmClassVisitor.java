package vip.ruoyun.template.asm;

import java.util.Arrays;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import vip.ruoyun.template.utils.LogM;

public class AsmClassVisitor extends ClassVisitor {

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
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        LogM.log("=====---------- visitAnnotation ----------=====");
        LogM.log(desc);
        return super.visitAnnotation(desc, visible);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        LogM.log("=====---------- visitMethod ----------=====");
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        return new AsmMethodVisitor(api, mv, access, name, desc);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        LogM.log("=====---------- visitEnd ----------=====");
    }

}