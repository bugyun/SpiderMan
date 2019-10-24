package vip.ruoyun.test;

import org.gradle.api.Project;
import vip.ruoyun.plugin.SpiderManTransform;
import vip.ruoyun.plugin.core.IAsmReader;

public class TestTransform extends SpiderManTransform {

    public TestTransform(final Project project) {
        super(project);
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public IAsmReader getAsmReader() {
        return new AsmHelper();
    }
}
