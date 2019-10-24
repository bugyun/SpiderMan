package vip.ruoyun.track.core;

import org.gradle.api.Project;
import vip.ruoyun.plugin.SpiderManTransform;
import vip.ruoyun.plugin.core.IAsmReader;
import vip.ruoyun.track.ext.TrackerExt;

public class TrackerTransform extends SpiderManTransform {

    private TrackerExt mTrackerExt;

    public TrackerTransform(final Project project) {
        super(project);
        mTrackerExt = project.getExtensions().getByType(TrackerExt.class);
    }

    @Override
    public boolean isOpen() {
        return mTrackerExt.isOpen();
    }

    @Override
    public IAsmReader getAsmReader() {
        return new AsmHelper();
    }
}
