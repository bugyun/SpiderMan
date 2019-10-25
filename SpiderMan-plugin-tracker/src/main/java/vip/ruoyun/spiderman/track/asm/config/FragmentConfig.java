package vip.ruoyun.spiderman.track.asm.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import vip.ruoyun.plugin.utils.LogM;

/**
 * 关于 fragment 的所有配置
 */
public class FragmentConfig implements Opcodes {

    public static final ImmutableList<String> supportFragmentClassList = ImmutableList.<String>builder()
            .add(
                    //Fragment
                    "android/support/v4/app/Fragment",
                    "android/support/v4/app/ListFragment",
                    "android/support/v4/app/DialogFragment",
                    // For AndroidX Fragment
                    "androidx/fragment/app/Fragment",
                    "androidx/fragment/app/ListFragment",
                    "androidx/fragment/app/DialogFragment"
            ).build();

    public final static ImmutableMap<String, MethodCell> sFragmentMethods = ImmutableMap.<String, MethodCell>builder()
            .put("onResume()V", new MethodCell(
                    "onResume",
                    "()V",
                    "onResume",
                    "(##)V",//Ljava/lang/Object;
                    0,
                    1,
                    Opcodes.ALOAD)
            ).put("setUserVisibleHint(Z)V", new MethodCell(
                    "setUserVisibleHint",
                    "(Z)V",
                    "setUserVisibleHint",
                    "(##Z)V",//Ljava/lang/Object;
                    0,
                    2,
                    Opcodes.ALOAD,
                    Opcodes.ILOAD)
            ).put("onHiddenChanged(Z)V", new MethodCell(
                    "onHiddenChanged",
                    "(Z)V",
                    "onHiddenChanged",
                    "(##Z)V",//Ljava/lang/Object;
                    0,
                    2,
                    Opcodes.ALOAD,
                    Opcodes.ILOAD)
            ).put("onPause()V", new MethodCell(
                    "onPause",
                    "()V",
                    "onPause",
                    "(##)V",//Ljava/lang/Object;
                    0,
                    1,
                    Opcodes.ALOAD)
            ).build();


    public static boolean fragmentMethod(String methodName, String superName, final MethodVisitor mv) {
        MethodCell methodCell = sFragmentMethods.get(methodName);
        if (methodCell != null) {
            for (int i = 0; i < methodCell.opcodes.length; i++) {
                mv.visitVarInsn(methodCell.opcodes[i], methodCell.paramsStart + i);
            }
            LogM.hint(methodCell.toString());
            LogM.hint(methodCell.agentDesc.replace("##", "L" + superName + ";"));
            mv.visitMethodInsn(INVOKESTATIC,
                    "vip/ruoyun/track/core/SpiderManTracker",
                    methodCell.agentName,
                    methodCell.agentDesc.replace("##", "L" + superName + ";"),
                    false);
            return true;
        }
        return false;
    }


    //补充方法
    public static void fragmentMethodAndSuper(final MethodCell methodCell, ClassVisitor cv, final String superName) {
        MethodVisitor methodVisitor = cv
                .visitMethod(ACC_PUBLIC, methodCell.name, methodCell.desc, null, null);
        methodVisitor.visitCode();
        for (int i = 0; i < methodCell.opcodes.length; i++) {
            methodVisitor.visitVarInsn(methodCell.opcodes[i], methodCell.paramsStart + i);
        }
        //super
        methodVisitor.visitMethodInsn(INVOKESPECIAL, superName, methodCell.name, methodCell.desc,
                false);
        for (int i = 0; i < methodCell.opcodes.length; i++) {
            methodVisitor.visitVarInsn(methodCell.opcodes[i], methodCell.paramsStart + i);
        }
        methodVisitor.visitMethodInsn(INVOKESTATIC,
                "vip/ruoyun/track/core/SpiderManTracker",
                methodCell.agentName,
                methodCell.agentDesc.replace("##", "L" + superName + ";"),
                false);
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(methodCell.opcodes.length, methodCell.opcodes.length);
        methodVisitor.visitEnd();
    }

}
