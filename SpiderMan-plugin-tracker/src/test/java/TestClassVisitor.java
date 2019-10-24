import java.io.IOException;
import org.junit.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

public class TestClassVisitor {

    @Test
    public void testVisitorRunnable() throws IOException {
        ClassPrinter cp = new ClassPrinter(Opcodes.ASM7);
        ClassReader cr = new ClassReader("java.lang.Runnable");//className
        cr.accept(cp, ClassReader.EXPAND_FRAMES);//SKIP_CODE
        //结果
        //java/lang/Runnable extend java/lang/Object {
        // run()V
        //}
    }

    @Test
    public void testVisitorString() throws IOException {
        ClassPrinter cp = new ClassPrinter(Opcodes.ASM7);
        ClassReader cr = new ClassReader("java.lang.String");//className
        cr.accept(cp, 0);
    }


}
