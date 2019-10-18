package vip.ruoyun.template;

import com.android.build.gradle.AppExtension;
import java.util.Collections;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import vip.ruoyun.template.core.TemplateTransform;
import vip.ruoyun.template.ext.TemplateExt;
import vip.ruoyun.template.utils.LogM;

public class TemplatePlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        if (!project.getPlugins().hasPlugin("com.android.application")) {
            throw new GradleException("SpiderManPlugin: Android Application plugin required");
        }
        TemplateExt templateMode = project.getExtensions().create("templateMode", TemplateExt.class);
        AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
        appExtension.registerTransform(new TemplateTransform(project, templateMode), Collections.EMPTY_LIST);
    }
}
