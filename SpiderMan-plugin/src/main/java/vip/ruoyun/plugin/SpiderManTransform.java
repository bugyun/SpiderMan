package vip.ruoyun.plugin;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import org.gradle.api.Project;

import java.util.Set;

public class SpiderManTransform extends Transform {

    private Project project;

    SpiderManTransform(Project project) {
        this.project = project;
    }


    @Override
    public String getName() {
        return null;
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return null;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return null;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }
}
