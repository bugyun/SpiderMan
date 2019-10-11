import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;


/**
 * ClassPrinter cp = new ClassPrinter();
 * ClassReader cr = new ClassReader("java.lang.Runnable");
 * cr.accept(cp, 0);
 *
 * 结果：
 * java/lang/Runnable extends java/lang/Object {
 * run()V
 * }
 */
public class ClassPrinter extends ClassVisitor {

    public ClassPrinter(final int api) {
        super(api);
    }

    public ClassPrinter(final int api, final ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature,
            final String superName, final String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        System.out.println(name + " extend " + superName + " {");
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        System.out.println(" " + desc + " " + name);
        return null;
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        System.out.println(" " + name + desc);
        return null;
    }

    @Override
    public void visitEnd() {
        System.out.println("}");
    }
}

