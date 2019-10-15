package vip.ruoyun.plugin;

import com.android.build.api.transform.Transform;
import com.android.build.gradle.AppExtension;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Collections;
import java.util.Iterator;
import java.util.ServiceLoader;

public class SpiderManPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        if (!project.getPlugins().hasPlugin("com.android.application")) {
            throw new GradleException("SpiderManPlugin: Android Application plugin required");
        }

        //当前可以获取
        AppExtension appExtension = (AppExtension) project.getExtensions().getByName("android");
        appExtension.registerTransform(new SpiderManTransform(project), Collections.EMPTY_LIST);

        //spi 动态发现 来加载不同的插件 Transform和 task
        //会加载 META-INF/services/com.android.build.api.transform.Transform 文件中的内容
        //com.mogujie.uni.sl.Pig
        //com.mogujie.uni.sl.Dog
        ServiceLoader<Transform> load = ServiceLoader.load(Transform.class);

        Iterator<Transform> iterator = load.iterator();
        while (iterator.hasNext()) {
            Transform next = iterator.next();
//            next.transform();
        }
        //获取
        appExtension.getApplicationVariants().forEach(applicationVariant -> {

        });
    }
}
