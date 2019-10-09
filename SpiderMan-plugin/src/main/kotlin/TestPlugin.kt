import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

open class GreetingPluginExtension {
    var message = "Hello from GreetingPlugin"
}

class TestPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        when {
            project.plugins.hasPlugin("com.android.application") -> {//是 Android 主项目
                project.extensions.getByName("android") as AppExtension
                project.getAndroid<AppExtension>().let {
                }
            }
            project.plugins.hasPlugin("com.android.library") -> {//是 Android 子项目
                project.getAndroid<LibraryExtension>().let {
                }
            }
        }
        var extension = project.extensions.create("greeting", GreetingPluginExtension::class.java)
//        extension = project.extensions.create<GreetingPluginExtension>("greeting")
    }
}

