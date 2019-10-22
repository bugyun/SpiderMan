package vip.ruoyun.template.asm;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import jdk.internal.org.objectweb.asm.Opcodes;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;
import vip.ruoyun.template.utils.AsmUtils;

/**
 * fragment 方法检测
 */
class FragmentMethodVisitor extends AdviceAdapter {

    static final List<String> supportFragmentClassList = Collections.unmodifiableList(
            Arrays.asList(
                    //Fragment
                    "android/support/v4/app/Fragment",
                    "android/support/v4/app/ListFragment",
                    "android/support/v4/app/DialogFragment",
                    // For AndroidX Fragment
                    "androidx/fragment/app/Fragment",
                    "androidx/fragment/app/ListFragment",
                    "androidx/fragment/app/DialogFragment"
            ));


    public final static HashMap<String, MethodCell> sFragmentMethods = new HashMap<>();

    static {
        sFragmentMethods.put("onResume()V", new MethodCell(
                "onResume",
                "()V",
                "",// parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
                "trackFragmentResume",
                "(Ljava/lang/Object;)V",
                0, 1,
                Collections.singletonList(Opcodes.ALOAD)));

        sFragmentMethods.put("setUserVisibleHint(Z)V", new MethodCell(
                "setUserVisibleHint",
                "(Z)V",
                "",// parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
                "trackFragmentSetUserVisibleHint",
                "(Ljava/lang/Object;Z)V",
                0, 2,
                Arrays.asList(Opcodes.ALOAD, Opcodes.ILOAD)));

        sFragmentMethods.put("onHiddenChanged(Z)V", new MethodCell(
                "onHiddenChanged",
                "(Z)V",
                "",
                "trackOnHiddenChanged",
                "(Ljava/lang/Object;Z)V",
                0, 2,
                Arrays.asList(Opcodes.ALOAD, Opcodes.ILOAD)));
        sFragmentMethods.put("onViewCreated(Landroid/view/View;Landroid/os/Bundle;)V", new MethodCell(
                "onViewCreated",
                "(Landroid/view/View;Landroid/os/Bundle;)V",
                "",
                "onFragmentViewCreated",
                "(Ljava/lang/Object;Landroid/view/View;Landroid/os/Bundle;)V",
                0, 3,
                Arrays.asList(Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD)));

        sFragmentMethods.put("onDestroy()V", new MethodCell(
                "onDestroy",
                "()V",
                "",// parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
                "trackFragmentDestroy",
                "(Ljava/lang/Object;)V",
                0, 1,
                Collections.singletonList(Opcodes.ALOAD)));
    }


    private final int access;

    FragmentMethodVisitor(final int api, final MethodVisitor methodVisitor, final int access,
            final String name,
            final String descriptor) {
        super(api, methodVisitor, access, name, descriptor);
        this.access = access;
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        if (!(AsmUtils.isPublic(access) && !AsmUtils.isStatic(access))) {
            return;//如果不是 public 非 static 的方法，直接 return
        }


    }
}
