import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent.ContentType
import com.android.build.api.transform.QualifiedContent.Scope
import com.android.build.api.transform.Status.ADDED
import com.android.build.api.transform.Status.CHANGED
import com.android.build.api.transform.Status.NOTCHANGED
import com.android.build.api.transform.Status.REMOVED
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import org.gradle.api.Transformer
import org.gradle.workers.WorkerExecutor

class TestTransform : Transform() {
    override fun getName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInputTypes(): MutableSet<ContentType> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isIncremental(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getScopes(): MutableSet<in Scope> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun transform(transformInvocation: TransformInvocation) {
        super.transform(transformInvocation)

        val inputs = transformInvocation.inputs
        val outputProvider = transformInvocation.outputProvider





        inputs.parallelStream().forEach { it ->

            it.jarInputs.parallelStream().filter { it.status != NOTCHANGED }.forEach { jarInput ->
                when (jarInput.status) {
                    REMOVED -> jarInput.file.delete()
                    CHANGED, ADDED -> {
                        val root = outputProvider?.getContentLocation(
                            jarInput.name,
                            jarInput.contentTypes,
                            jarInput.scopes,
                            Format.JAR
                        )
//                        jarInput.file.transform(root) { bytecode ->
//                            bytecode.transform(this)
//                        }
                    }

                }
            }

        }

        inputs?.forEach {

        }
    }
}