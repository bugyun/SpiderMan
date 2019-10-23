package vip.ruoyun.template.asm;

import java.util.HashSet;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;
import vip.ruoyun.template.asm.config.FragmentConfig;

/**
 * fragment 方法检测
 */
class FragmentMethodVisitor extends AdviceAdapter {


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
        if (isWrite) {
            return;
        }
        String methodName = getName() + methodDesc;
        FragmentConfig.fragmentMethod(methodName, superName, mv);
        isWrite = true;
        visitedFragmentMethods.add(methodName);
    }
}
