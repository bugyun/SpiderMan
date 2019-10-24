import com.android.build.gradle.BaseExtension
import com.android.repository.Revision
import org.gradle.api.Project

/**
 *  didi gradle 开源项目
 */
inline fun <reified T : BaseExtension> Project.getAndroid(): T = extensions.getByName("android") as T

/**
 * The gradle version
 */
val Project.gradleVersion: Revision
    get() = Revision.parseRevision(gradle.gradleVersion)