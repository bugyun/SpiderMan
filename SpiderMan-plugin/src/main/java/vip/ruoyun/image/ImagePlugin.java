package vip.ruoyun.image;

import com.android.build.gradle.AppExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class ImagePlugin implements Plugin<Project> {

    private Logger logger;

    @Override
    public void apply(@NotNull Project project) {
        logger = project.getLogger();
        AppExtension appExtension = (AppExtension) project.getProperties().get("android");
        appExtension.registerTransform(new ImageTransform(project), Collections.EMPTY_LIST);
    }
}
