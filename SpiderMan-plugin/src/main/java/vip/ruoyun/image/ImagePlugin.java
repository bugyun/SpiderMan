package vip.ruoyun.image;

import com.android.build.api.transform.Transform;
import com.android.build.gradle.AppExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.ServiceLoader;

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


        //日志类型，从下面的结果看
        project.getLogger().isInfoEnabled();
        project.getLogger().debug("debug");
        project.getLogger().trace("trace");
        project.getLogger().info("info");
        project.getLogger().warn("warn");
        project.getLogger().error("error");
        System.out.println("开始");
        project.getLogger().error("-------------------TemplatePlugin开始-------------------");

        //warn
        //error
        //-------------------TemplatePlugin开始-------------------
        //开始
        //-------------------TemplatePlugin开始-------------------
    }
}
