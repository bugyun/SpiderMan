package vip.ruoyun.spiderman.track.asm.config;

import com.google.common.collect.ImmutableMap;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ViewConfig {


    // methodVisitor.visitVarInsn(ALOAD, 1);
    //   methodVisitor.visitMethodInsn(INVOKESTATIC, "vip/ruoyun/track/core/SpiderManTracker", "trackView",
    //                    "(Landroid/view/View;)V", false);
    public final static ImmutableMap<String, MethodCell> sViewMethods = ImmutableMap.<String, MethodCell>builder()
            .put("onClick(Landroid/view/View;)V", new MethodCell(
                    "onClick",
                    "(Landroid/view/View;)V",
                    AsmConfig.ASM_PAGE_CLASS_NAME,
                    "trackView",
                    "(Landroid/view/View;)V",
                    1,
                    1,
                    Opcodes.ALOAD)
            ).build();

    public final static ImmutableMap<String, MethodCell> sViewLambdaMethods
            = ImmutableMap.<String, MethodCell>builder()
            .put("(Landroid/view/View;)V", new MethodCell(
                    "onClick",
                    "(Landroid/view/View;)V",
                    AsmConfig.ASM_PAGE_CLASS_NAME,
                    "trackView",
                    "(Landroid/view/View;)V",
                    1,
                    1,
                    Opcodes.ALOAD)
            ).build();

    public static void method(MethodVisitor mv) {
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, AsmConfig.ASM_PAGE_CLASS_NAME, "trackView",
                "(Landroid/view/View;)V", false);
    }

    public static void lambdaMethod(MethodVisitor mv) {
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, AsmConfig.ASM_PAGE_CLASS_NAME, "trackView",
                "(Landroid/view/View;)V", false);
    }



}
