package vip.ruoyun.plugin;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Status;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;
import vip.ruoyun.plugin.core.HandleHelper;
import vip.ruoyun.plugin.core.IAsmReader;

public abstract class SpiderManTransform extends Transform {

    protected final Project project;

    private HandleHelper mHandleHelper;


    private ForkJoinPool executor = new ForkJoinPool(Runtime.getRuntime().availableProcessors(),
            ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);

    public SpiderManTransform(final Project project) {
        this.project = project;
    }

    @SuppressWarnings("all")
    @Override
    public final void transform(final TransformInvocation transformInvocation)
            throws TransformException, InterruptedException, IOException {
        mHandleHelper = new HandleHelper(getAsmReader());
        super.transform(transformInvocation);
        long startTime = System.currentTimeMillis();
        Collection<TransformInput> inputs = transformInvocation.getInputs();//消费型输入，可以从中获取jar包和class文件夹路径。需要输出给下一个任务
        TransformOutputProvider outputProvider = transformInvocation
                .getOutputProvider();//OutputProvider管理输出路径，如果消费型输入为空，你会发现OutputProvider == null
        boolean isIncremental = transformInvocation.isIncremental();  //当前是否是增量编译
        if (!isIncremental) {//如果不是自动增长,就删除文件 DexBuilderFolder 和 OutputFolder
            executor.execute(() -> mHandleHelper.cleanDexBuilderFolder(project.getBuildDir().getAbsolutePath()));
            executor.execute(() -> mHandleHelper.cleanOutputFolder(outputProvider));
            executor.awaitQuiescence(1, TimeUnit.MINUTES);
        }

        for (TransformInput input : inputs) {
            for (JarInput jarInput : input.getJarInputs()) {
                File dest = outputProvider.getContentLocation(
                        jarInput.getFile().getAbsolutePath(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR);
                if (isIncremental) {
                    Status status = jarInput.getStatus();//获取状态
                    switch (status) {
                        case NOTCHANGED:
                            break;
                        case ADDED:
                        case CHANGED:
                            handleJarInput(jarInput.getFile(), dest);
                            break;
                        case REMOVED:
                            if (dest.exists()) {
                                FileUtils.forceDelete(dest);
                            }
                            break;
                    }
                } else {
                    handleJarInput(jarInput.getFile(), dest);
                }
            }

            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                File dest = outputProvider.getContentLocation(
                        directoryInput.getName(),
                        directoryInput.getContentTypes(),
                        directoryInput.getScopes(),
                        Format.DIRECTORY);
                FileUtils.forceMkdir(dest);//创建文件夹
                if (isIncremental) {
                    String srcDirPath = directoryInput.getFile().getAbsolutePath();
                    String destDirPath = dest.getAbsolutePath();
                    Map<File, Status> fileStatusMap = directoryInput.getChangedFiles();
                    for (Map.Entry<File, Status> changedFile : fileStatusMap.entrySet()) {
                        Status status = changedFile.getValue();
                        File inputFile = changedFile.getKey();
                        File destFile = new File(inputFile.getAbsolutePath().replace(srcDirPath, destDirPath));
                        switch (status) {
                            case NOTCHANGED:
                                break;
                            case REMOVED:
                                if (destFile.exists()) {
                                    destFile.delete();
                                }
                                break;
                            case ADDED:
                            case CHANGED:
                                try {
                                    FileUtils.touch(destFile);
                                } catch (IOException e) {
                                    //maybe mkdirs fail for some strange reason, try again.
                                    Files.createParentDirs(destFile);
                                }
                                handleSingleFile(inputFile, destFile, srcDirPath);
                                break;
                        }
                    }
                } else {
                    handleDirectory(directoryInput.getFile(), dest);//将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
                }
            }
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }

    private void handleDirectory(File inputDir, File outputDir) {
        if (isOpen()) {
            final String inputDirPath = inputDir.getAbsolutePath();
            final String outputDirPath = outputDir.getAbsolutePath();
            if (inputDir.isDirectory()) {
                for (final File file : com.android.utils.FileUtils.getAllFiles(inputDir)) {
                    executor.execute(() -> {
                        String filePath = file.getAbsolutePath();
                        File outputFile = new File(filePath.replace(inputDirPath, outputDirPath));
                        try {
                            mHandleHelper.handleSingleClassToFile(file, outputFile, inputDirPath, isOpen());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } else {
            executor.execute(() -> {
                try {
                    FileUtils.copyDirectory(inputDir, outputDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void handleSingleFile(final File inputFile, final File destFile, final String srcDirPath) {
        executor.execute(() -> {
            try {
                mHandleHelper.handleSingleClassToFile(inputFile, destFile, srcDirPath, isOpen());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleJarInput(final File srcJar, final File destJar) {
        executor.execute(() -> {
            try {
                if (isOpen()) {
                    mHandleHelper.handleJar(srcJar, destJar);
                } else {
                    FileUtils.copyFile(srcJar, destJar);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return true;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isCacheable() {
        return true;
    }

    public abstract boolean isOpen();

    public abstract IAsmReader getAsmReader();
}
