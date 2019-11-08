package vip.ruoyun.spiderman.track;

import com.android.build.gradle.AppExtension;
import java.util.Collections;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import vip.ruoyun.spiderman.track.core.AutoTrackerTransform;
import vip.ruoyun.spiderman.track.ext.AutoTrackerExt;

public class AutoTrackerPlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        if (!project.getPlugins().hasPlugin("com.android.application")) {
            throw new GradleException("TrackerPlugin: Android Application plugin required");
        }
        project.getExtensions().create("autoTracker", AutoTrackerExt.class);
        AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
        appExtension.registerTransform(new AutoTrackerTransform(project), Collections.EMPTY_LIST);
    }
}
