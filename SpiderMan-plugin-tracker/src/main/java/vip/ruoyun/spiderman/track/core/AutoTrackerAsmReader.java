package vip.ruoyun.spiderman.track.core;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import vip.ruoyun.plugin.core.SpiderManAsmReader;
import vip.ruoyun.spiderman.track.asm.AsmClassVisitor;

public class AutoTrackerAsmReader extends SpiderManAsmReader {

    @Override
    public ClassVisitor createClassVisitor(final ClassWriter classWriter) {
        return new AsmClassVisitor(classWriter);
    }
}
