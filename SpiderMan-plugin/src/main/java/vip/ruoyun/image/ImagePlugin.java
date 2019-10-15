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


//获取 AppExtension 的三种方式
//        AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);//gradle 3.5
//        AppExtension appExtension = (AppExtension) project.getExtensions().getByName("android");//会报异常
//        AppExtension appExtension = (AppExtension) project.getProperties().get("android");//不会报异常,如果获取不到,就返回 null
//        if (appExtension != null) {
//            appExtension.registerTransform(new ImageTransform(project), Collections.EMPTY_LIST);
//        }
    }
}
