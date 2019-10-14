package vip.ruoyun.image;

import com.android.build.api.transform.*;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.utils.FileUtils;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ImageTransform extends Transform {
    private Logger logger;
    private Project project;

    public ImageTransform(Project project) {
        this.project = project;
        this.logger = project.getLogger();
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
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
    public boolean isCacheable() {
        return true;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        //当前是否是增量编译
        boolean isIncremental = transformInvocation.isIncremental();


        //引用型输入，无需输出。
        Collection<TransformInput> referencedInputs = transformInvocation.getReferencedInputs();

        log("referencedInputs :num" + referencedInputs.size());
        for (TransformInput input : referencedInputs) {
            for (JarInput jarInput : input.getJarInputs()) {
                log("referencedInputs :JarInput :begin");
                log(jarInput.getFile().getAbsolutePath());
            }
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                log("referencedInputs : DirectoryInput :begin");
                log(directoryInput.getFile().getAbsolutePath());
            }
        }

        //返回自上次以来二级文件更改的列表。仅此转换可以增量处理的辅助文件将成为此更改集的一部分。
        Collection<SecondaryInput> secondaryInputs = transformInvocation.getSecondaryInputs();
        log("SecondaryInput :num" + secondaryInputs.size());

        for (SecondaryInput secondaryInput : secondaryInputs) {
            log("SecondaryInput :begin");
            log(secondaryInput.getSecondaryInput().getFileCollection(project).getAsPath());
            log(secondaryInput.getSecondaryInput().getFile().getAbsolutePath());
        }


        //消费型输入，可以从中获取jar包和class文件夹路径。需要输出给下一个任务
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        //OutputProvider管理输出路径，如果消费型输入为空，你会发现OutputProvider == null
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        for (TransformInput input : inputs) {
            for (JarInput jarInput : input.getJarInputs()) {
                File dest = outputProvider.getContentLocation(
                        jarInput.getFile().getAbsolutePath(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR);

                //我们的子 module 是通过 jar 的形式添加到 主 module 中的
                //JarInput :begin
                ///Users/ruoyun/Downloads/github/KotlinAAC/app/build/intermediates/transforms/ImageTransform/debug/111.jar
                ///Users/ruoyun/Downloads/github/KotlinAAC/baselib/build/intermediates/runtime_library_classes/debug/classes.jar

                //jarInput 第三方库的 jar
                //jarInput.getFile() :/Users/ruoyun/.gradle/caches/transforms-2/files-2.1/07afa6b210e275aa1c0c37f3bc807615/lifecycle-livedata-core-2.2.0-alpha05/jars/classes.jar
                //dest: /Users/ruoyun/Downloads/github/KotlinAAC/app/build/intermediates/transforms/ImageTransform/debug/83.jar
                log("JarInput :begin");
                log(dest.getAbsolutePath());
                log(jarInput.getFile().getAbsolutePath());
                //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了

                FileUtils.copyFile(jarInput.getFile(), dest);
//                JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(dest)); //输出路径
//
//                JarFile jarFile = new JarFile(jarInput.getFile());
//                Enumeration<JarEntry> entries = jarFile.entries();
//                while (entries.hasMoreElements()) {
//                    JarEntry jarEntry = entries.nextElement();
//
//                    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
//                    ClassVisitor adapter = new AutoClassVisitor(classWriter);
//                    ClassReader cr = new ClassReader(jarEntry.getName());
//                    cr.accept(adapter, ClassReader.EXPAND_FRAMES);
//
//
//                    ZipEntry zipEntry = new ZipEntry(jarEntry.getName());
//                    jarOutputStream.putNextEntry(zipEntry);
//                    jarOutputStream.write(classWriter.toByteArray());
//                    jarOutputStream.closeEntry();
//                    jarOutputStream.flush();
//                }
//                jarFile.close();
//                jarOutputStream.close();
            }
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                File dest = outputProvider.getContentLocation(directoryInput.getName(),
                        directoryInput.getContentTypes(), directoryInput.getScopes(),
                        Format.DIRECTORY);
                //本地javac代码 class
                //DirectoryInput :begin
                //directoryInput.getFile(): /Users/ruoyun/Downloads/github/KotlinAAC/app/build/intermediates/javac/debug/classes
                //dest: /Users/ruoyun/Downloads/github/KotlinAAC/app/build/intermediates/transforms/ImageTransform/debug/101
                //DirectoryInput :begin
                ///Users/ruoyun/Downloads/github/KotlinAAC/app/build/intermediates/transforms/ImageTransform/debug/102
                ///Users/ruoyun/Downloads/github/KotlinAAC/app/build/tmp/kotlin-classes/debug
                //DirectoryInput :begin
                //dest : /Users/ruoyun/Downloads/github/KotlinAAC/app/build/intermediates/transforms/ImageTransform/debug/103
                //directoryInput.getFile() :/Users/ruoyun/Downloads/github/KotlinAAC/app/build/tmp/kapt3/classes/debug
                log("DirectoryInput :begin");
                log(dest.getAbsolutePath());
                log(directoryInput.getFile().getAbsolutePath());

                //clean build getChangedFiles 的 size 为 0
                Map<File, Status> fileStatusMap = directoryInput.getChangedFiles();
                log("DirectoryInput :num" + fileStatusMap.size());

                for (Map.Entry<File, Status> changedFile : fileStatusMap.entrySet()) {
                    Status status = changedFile.getValue();
                    File inputFile = changedFile.getKey();
                    log(inputFile.getAbsoluteFile());
                    switch (status) {
                        case NOTCHANGED:
                            break;
                        case REMOVED:
                            break;
                        case ADDED:
                        case CHANGED:
                            break;
                    }
                }
                //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
                FileUtils.copyDirectory(directoryInput.getFile(), dest);
//                if (directoryInput.getFile().isDirectory()) {
//                    directoryInput.getFile().listFiles();
//
//                }
//
//                FileInputStream is = new FileInputStream(directoryInput.getFile());
//                ClassReader cr = new ClassReader(directoryInput.getFile().getAbsolutePath());
//                ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
//                cr.accept(cw, 0);
//                FileOutputStream fos = new FileOutputStream(dest);
//                fos.write(cw.toByteArray());
//                fos.close();
            }
        }

//        ClassReader
//                ClassWriter
//        ClassVisitor
//        AdviceAdapter
    }


//    static byte[] modifyClasses(String className, byte[] srcByteCode) {
//        byte[] classBytesCode = null
//        try {
//            classBytesCode = modifyClass(srcByteCode)
//            //调试模式下再遍历一遍看修改的方法情况
////            if (Logger.isDebug()) {
////                seeModifyMethod(classBytesCode)
////            }
//            return classBytesCode
//        } catch (Exception e) {
//            e.printStackTrace()
//        }
//        if (classBytesCode == null) {
//            classBytesCode = srcByteCode
//        }
//        return classBytesCode
//    }

    /**
     * 真正修改类中方法字节码
     */
    private static byte[] modifyClass(byte[] srcClass) throws IOException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
//        ClassVisitor adapter = new AutoClassVisitor(classWriter);
//        ClassReader cr = new ClassReader(srcClass);
//        cr.accept(adapter, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }

    private void log(Object object) {
        System.out.println(object);
    }
}
