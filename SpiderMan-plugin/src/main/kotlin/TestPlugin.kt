import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.builder.model.AndroidProject
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

open class GreetingPluginExtension {
    var message = "Hello from GreetingPlugin"
}

class TestPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        println(project.rootProject)
        println(project.rootDir)
        println(project.buildDir)
        println(project.buildFile)
        println(project.parent)
        println(project.name)
        println(project.displayName)
        println(project.description)
        println(project.group)
        println(project.version)
        println(project.status)
        println(project.childProjects)
        println(project.project)
        println(project.allprojects)
        println(project.subprojects)
        println(project.path)
        println(project.defaultTasks)
        println(project.projectDir)
        println(project.providers)
        println(project.objects)
        println(project.layout)

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

        project.buildDir.file(AndroidProject.FD_INTERMEDIATES, "transforms", "dexBuilder").let { dexBuilder ->
            if (dexBuilder.exists()) {
                dexBuilder.deleteRecursively()
            }
        }

        //intermediates transforms dexBuilder
        javaClass.classLoader
        var extension = project.extensions.create("greeting", GreetingPluginExtension::class.java)
//        extension = project.extensions.create<GreetingPluginExtension>("greeting")
    }
}

fun File.file(vararg path: String) = File(this, path.joinToString(File.separator))

