package vip.ruoyun.plugin;

import com.android.build.gradle.AppExtension;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Collections;

public class SpiderManPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        if (!project.getPlugins().hasPlugin("com.android.application")) {
            throw new GradleException("SpiderManPlugin: Android Application plugin required");
        }
        AppExtension appExtension = (AppExtension) project.getExtensions().getByName("android");
        appExtension.registerTransform(new SpiderManTransform(project), Collections.EMPTY_LIST);
    }
}
