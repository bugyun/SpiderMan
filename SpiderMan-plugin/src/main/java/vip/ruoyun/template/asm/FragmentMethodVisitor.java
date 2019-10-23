package vip.ruoyun.template.asm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.HashSet;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * fragment 方法检测
 */
class FragmentMethodVisitor extends AdviceAdapter {

    static final ImmutableList<String> supportFragmentClassList = ImmutableList.<String>builder()
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

    final static ImmutableMap<String, MethodCell> sFragmentMethods = ImmutableMap.<String, MethodCell>builder()
            .put("onResume()V", new MethodCell(
                    "onResume",
                    "()V",
                    "trackFragmentResume",
                    "(##)V",//Ljava/lang/Object;
                    0,
                    1,
                    Opcodes.ALOAD)
            ).put("setUserVisibleHint(Z)V", new MethodCell(
                    "setUserVisibleHint",
                    "(Z)V",
                    "trackFragmentSetUserVisibleHint",
                    "(##Z)V",//Ljava/lang/Object;
                    0,
                    2,
                    Opcodes.ALOAD,
                    Opcodes.ILOAD)
            ).put("onHiddenChanged(Z)V", new MethodCell(
                    "onHiddenChanged",
                    "(Z)V",
                    "trackOnHiddenChanged",
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

    private final String superName;

    private boolean isWrite = false;

    private HashSet<String> visitedFragmentMethods;

    FragmentMethodVisitor(final int api, final MethodVisitor methodVisitor, final int access,
            final String name,
            final String descriptor, final String superName,
            final HashSet<String> visitedFragmentMethods) {
        super(api, methodVisitor, access, name, descriptor);
        this.superName = superName;
        this.visitedFragmentMethods = visitedFragmentMethods;
    }


    @Override
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String descriptor,
            final boolean isInterface) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        if (opcode == INVOKESPECIAL && owner.equals(superName)) { //super()方法之后，写入方法
            write();
        }
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
    }

    /**
     * 问题:
     * 当在方法体中做了一些操作,那么如果要让界面消失,那么 getView 中的状态view 的状态可能就消失了.所以关于 view 的方法必须放到前面.
     * fragment 的话,可以放到后面,因为没有引用消失
     */
    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
        write();
    }

    private void write() {
        String methodName = getName() + methodDesc;
        switch (methodName) {//如果没有 super() 方法的话，就在直接写入
            //void test(String str) --> methodName:"test"   methodDes:"(Ljava/lang/String;)V"
            case "onResume()V":
            case "onPause()V":
                if (isWrite) {
                    return;
                }
                mv.visitVarInsn(ALOAD, 0);
                mv.visitMethodInsn(INVOKESTATIC, "vip/ruoyun/track/core/SpiderManTracker", "onResume",
                        "(L" + superName + ";)V", false);
                isWrite = true;
                visitedFragmentMethods.add(methodName);
                break;
            case "setUserVisibleHint(Z)V":
                if (isWrite) {
                    return;
                }
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(ILOAD, 1);
                mv.visitMethodInsn(INVOKESTATIC, "vip/ruoyun/track/core/SpiderManTracker", "setUserVisibleHint",
                        "(L" + superName + ";Z)V", false);
                isWrite = true;
                visitedFragmentMethods.add(methodName);
                break;
            case "onHiddenChanged(Z)V":
                if (isWrite) {
                    return;
                }
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(ILOAD, 1);
                mv.visitMethodInsn(INVOKESTATIC, "vip/ruoyun/track/core/SpiderManTracker", "onHiddenChanged",
                        "(L" + superName + ";Z)V", false);
                isWrite = true;
                visitedFragmentMethods.add(methodName);
                break;
            default:
                break;
        }
    }

}
