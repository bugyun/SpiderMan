package vip.ruoyun.template;

import com.android.build.api.transform.*;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.builder.model.AndroidProject;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class TemplateTransform extends Transform {

    private final TemplateMode mByType;

    private final Project project;

    private final Logger mLogger;

    private ForkJoinPool executor = new ForkJoinPool(Runtime.getRuntime().availableProcessors(),
            ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);

    public TemplateTransform(final Project project) {
        this.project = project;
        mLogger = project.getLogger();
        mByType = project.getExtensions().findByType(TemplateMode.class);
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
    public void transform(final TransformInvocation transformInvocation)
            throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        long startTime = System.currentTimeMillis();

        //消费型输入，可以从中获取jar包和class文件夹路径。需要输出给下一个任务
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        //OutputProvider管理输出路径，如果消费型输入为空，你会发现OutputProvider == null
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        //当前是否是增量编译
        boolean isIncremental = transformInvocation.isIncremental();
        if (!isIncremental) {
            executor.execute(() -> {
                Path path = Paths.get(project.getBuildDir().getAbsolutePath(), AndroidProject.FD_INTERMEDIATES, "transforms", "dexBuilder");
                LogM.log("路径:" + path.toString());
                File file = path.toFile();
                if (file.exists()) {
                    try {
                        com.android.utils.FileUtils.deleteDirectoryContents(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            executor.execute(() -> {
                try {
                    outputProvider.deleteAll();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
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
                            handleJarInput(jarInput.getFile(), dest, true);
                            break;
                        case REMOVED:
                            if (dest.exists()) {
                                FileUtils.forceDelete(dest);
                            }
                            break;
                    }
                } else {
                    handleJarInput(jarInput.getFile(), dest, true);
                }
            }


            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                File dest = outputProvider.getContentLocation(directoryInput.getName(),
                        directoryInput.getContentTypes(), directoryInput.getScopes(),
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
        long cost = (System.currentTimeMillis() - startTime) / 1000;
        LogM.log("执行时间" + cost + "秒");
    }

    private void handleDirectory(File inputDir, File outputDir) {
        final String inputDirPath = inputDir.getAbsolutePath();
        final String outputDirPath = outputDir.getAbsolutePath();
        if (inputDir.isDirectory()) {
            for (final File file : com.android.utils.FileUtils.getAllFiles(inputDir)) {
                executor.execute(() -> {
                    String filePath = file.getAbsolutePath();
                    File outputFile = new File(filePath.replace(inputDirPath, outputDirPath));
                    try {
                        weaveSingleClassToFile(file, outputFile, inputDirPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private void handleSingleFile(final File inputFile, final File destFile, final String srcDirPath) {
        executor.execute(() -> {
            try {
                weaveSingleClassToFile(inputFile, destFile, srcDirPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * @param isHandle 是否要处理，false 不处理，直接拷贝
     */
    private void handleJarInput(final File srcJar, final File destJar, boolean isHandle) {
        executor.execute(() -> {
            try {
                if (isHandle) {
                    weaveJar(srcJar, destJar);
                } else {
                    FileUtils.copyFile(srcJar, destJar);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    private void weaveJar(File inputJar, File outputJar) throws IOException {
        ZipFile inputZip = new ZipFile(inputJar);
        ZipOutputStream outputZip = new ZipOutputStream(new BufferedOutputStream(
                java.nio.file.Files.newOutputStream(outputJar.toPath())));
        Enumeration<? extends ZipEntry> inEntries = inputZip.entries();
        while (inEntries.hasMoreElements()) {
            ZipEntry entry = inEntries.nextElement();
            InputStream originalFile =
                    new BufferedInputStream(inputZip.getInputStream(entry));
            ZipEntry outEntry = new ZipEntry(entry.getName());
            byte[] newEntryContent;
            // seperator of entry name is always '/', even in windows
            if (!isWeavableClass(outEntry.getName().replace("/", "."))) {
                newEntryContent = IOUtils.toByteArray(originalFile);
            } else {
                newEntryContent = weaveSingleClassToByteArray(originalFile);
            }
            CRC32 crc32 = new CRC32();
            crc32.update(newEntryContent, 0, newEntryContent.length);
            outEntry.setCrc(crc32.getValue());
            outEntry.setMethod(ZipEntry.STORED);
            outEntry.setSize(newEntryContent.length);
            outEntry.setCompressedSize(newEntryContent.length);
            outEntry.setLastAccessTime(ZERO);
            outEntry.setLastModifiedTime(ZERO);
            outEntry.setCreationTime(ZERO);
            outputZip.putNextEntry(outEntry);
            outputZip.write(newEntryContent);
            outputZip.closeEntry();
        }
        outputZip.flush();
        outputZip.close();
    }

    private static final FileTime ZERO = FileTime.fromMillis(0);

    private byte[] weaveSingleClassToByteArray(InputStream inputStream) throws IOException {
        //开始处理，通过 ASM
        ClassReader classReader = new ClassReader(inputStream);
        //writer
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        //visitor
        AsmClassVisitor asmClassVisitor = new AsmClassVisitor(classWriter);
        //转换
        classReader.accept(asmClassVisitor, ClassReader.EXPAND_FRAMES);
        //输出字节码
        return classWriter.toByteArray();
    }


    private boolean isWeavableClass(String fullQualifiedClassName) {
        return fullQualifiedClassName.endsWith(".class") && !fullQualifiedClassName.contains("R$")
                && !fullQualifiedClassName.contains("R.class") && !fullQualifiedClassName
                .contains("BuildConfig.class");
    }

    private static final String FILE_SEP = File.separator;

    private void weaveSingleClassToFile(File inputFile, File outputFile, String inputBaseDir)
            throws IOException {
        if (!inputBaseDir.endsWith(FILE_SEP)) {
            inputBaseDir = inputBaseDir + FILE_SEP;
        }
        if (isWeavableClass(inputFile.getAbsolutePath().replace(inputBaseDir, "").replace(FILE_SEP, "."))) {
            FileUtils.touch(outputFile);
            InputStream inputStream = new FileInputStream(inputFile);
            byte[] bytes = weaveSingleClassToByteArray(inputStream);
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(bytes);
            fos.close();
            inputStream.close();
        } else {
            if (inputFile.isFile()) {
                FileUtils.touch(outputFile);
                FileUtils.copyFile(inputFile, outputFile);
            }
        }
    }

    private void log(final String message) {
        LogM.log(message);
        mLogger.info(message);
    }

}
