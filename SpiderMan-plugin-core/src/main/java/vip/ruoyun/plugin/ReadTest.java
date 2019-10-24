package vip.ruoyun.plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import vip.ruoyun.plugin.core.SpiderManAsmReader;

public class ReadTest extends SpiderManAsmReader {

    @Override
    public ClassVisitor createClassVisitor(final ClassWriter classWriter) {
        return new ClassVisitor(Opcodes.ASM7, classWriter) {
            @Override
            public void visit(final int version, final int access, final String name, final String signature,
                    final String superName,
                    final String[] interfaces) {
                super.visit(version, access, name, signature, superName, interfaces);
            }
        };
    }
}
