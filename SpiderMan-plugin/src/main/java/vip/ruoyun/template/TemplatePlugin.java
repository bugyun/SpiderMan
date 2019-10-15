package vip.ruoyun.template;

import com.android.build.gradle.AppExtension;
import java.util.Collections;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class TemplatePlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        project.getExtensions().create("templateMode", TemplateMode.class);
        AppExtension appExtension = (AppExtension) project.getProperties().get("android");
        appExtension.registerTransform(new TemplateTransform(project), Collections.EMPTY_LIST);
    }
}
