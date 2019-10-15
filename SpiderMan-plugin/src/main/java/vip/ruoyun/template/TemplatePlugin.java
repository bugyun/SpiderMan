package vip.ruoyun.template;

import com.android.build.gradle.AppExtension;

import java.util.Collections;

import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class TemplatePlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {

        if (!project.getPlugins().hasPlugin("com.android.application")) {
            throw new GradleException("SpiderManPlugin: Android Application plugin required");
        }

        project.getExtensions().create("templateMode", TemplateMode.class);
        AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
        appExtension.registerTransform(new TemplateTransform(project), Collections.EMPTY_LIST);
    }
}
