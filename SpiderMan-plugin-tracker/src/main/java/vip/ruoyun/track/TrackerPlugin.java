package vip.ruoyun.track;

import com.android.build.gradle.AppExtension;
import java.util.Collections;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import vip.ruoyun.track.core.TrackerTransform;
import vip.ruoyun.track.ext.TrackerExt;

public class TrackerPlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        if (!project.getPlugins().hasPlugin("com.android.application")) {
            throw new GradleException("TrackerPlugin: Android Application plugin required");
        }
        project.getExtensions().create("autoTracker", TrackerExt.class);
        AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
        appExtension.registerTransform(new TrackerTransform(project), Collections.EMPTY_LIST);
    }
}
