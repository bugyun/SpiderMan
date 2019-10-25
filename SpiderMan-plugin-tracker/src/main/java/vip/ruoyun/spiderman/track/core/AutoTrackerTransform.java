package vip.ruoyun.spiderman.track.core;

import org.gradle.api.Project;
import vip.ruoyun.plugin.SpiderManTransform;
import vip.ruoyun.plugin.core.IAsmReader;
import vip.ruoyun.plugin.utils.LogM;
import vip.ruoyun.spiderman.track.ext.AutoTrackerExt;

public class AutoTrackerTransform extends SpiderManTransform {

    private AutoTrackerExt mAutoTrackerExt;

    public AutoTrackerTransform(final Project project) {
        super(project);
        mAutoTrackerExt = project.getExtensions().getByType(AutoTrackerExt.class);
    }

    @Override
    public boolean isOpen() {
        return mAutoTrackerExt.isOpen();
    }

    @Override
    public IAsmReader getAsmReader() {
        LogM.isLog = mAutoTrackerExt.isLog();
        LogM.isHint = mAutoTrackerExt.isLog();
        return new AutoTrackerAsmReader();
    }
}
