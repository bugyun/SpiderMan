package vip.ruoyun.plugin;

import org.gradle.api.Project;
import vip.ruoyun.plugin.core.IClassReader;

public class Testtr extends SpiderManTransform {

    public Testtr(final Project project) {
        super(project);
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public IClassReader getClassReader() {
        return null;
    }

    @Override
    public void beginTransform() {

    }

    @Override
    public void endTransform() {

    }
}
