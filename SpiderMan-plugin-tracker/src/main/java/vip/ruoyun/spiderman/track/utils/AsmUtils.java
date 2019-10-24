package vip.ruoyun.spiderman.track.utils;

import org.objectweb.asm.Opcodes;

public class AsmUtils {

    public static boolean isSynthetic(int access) {
        return (access & Opcodes.ACC_SYNTHETIC) != 0;
    }

    public static boolean isPrivate(int access) {
        return (access & Opcodes.ACC_PRIVATE) != 0;
    }

    public static boolean isPublic(int access) {
        return (access & Opcodes.ACC_PUBLIC) != 0;
    }

    public static boolean isStatic(int access) {
        return (access & Opcodes.ACC_STATIC) != 0;
    }

    public static boolean isLambda(int access) {
        return isSynthetic(access)
                && isPrivate(access)
                && isStatic(access);
    }

}
