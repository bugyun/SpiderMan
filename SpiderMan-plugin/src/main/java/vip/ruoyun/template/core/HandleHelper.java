package vip.ruoyun.template.core;

import com.android.build.api.transform.TransformOutputProvider;
import com.android.builder.model.AndroidProject;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import vip.ruoyun.template.utils.LogM;

public class HandleHelper {

    /**
     * 处理 intermediates/transforms/dexBuilder 文件
     */
    public static void cleanDexBuilderFolder(String filePath) {
        Path path = Paths.get(filePath, AndroidProject.FD_INTERMEDIATES, "transforms",
                "dexBuilder");
        LogM.log("cleanDexBuilderFolder:" + path.toString());
        File file = path.toFile();
        if (file.exists()) {
            try {
                com.android.utils.FileUtils.deleteDirectoryContents(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除 OutputFolder 的文件
     */
    public static void cleanOutputFolder(final TransformOutputProvider outputProvider) {
        try {
            outputProvider.deleteAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final FileTime ZERO = FileTime.fromMillis(0);

    /**
     * 处理 jar 文件
     */
    public static void handleJar(File inputJar, File outputJar) throws IOException {
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
            if (!AsmHelper.isWeavableClass(outEntry.getName().replace("/", "."))) {
                newEntryContent = IOUtils.toByteArray(originalFile);
            } else {
                newEntryContent = AsmHelper.weaveSingleClassToByteArray(originalFile);
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

    private static final String FILE_SEP = File.separator;

    /**
     * 处理单个文件
     */
    public static void handleSingleClassToFile(File inputFile, File outputFile, String inputBaseDir)
            throws IOException {
        if (!inputBaseDir.endsWith(FILE_SEP)) {
            inputBaseDir = inputBaseDir + FILE_SEP;
        }
        if (AsmHelper.isWeavableClass(inputFile.getAbsolutePath().replace(inputBaseDir, "").replace(FILE_SEP, "."))) {
            org.apache.commons.io.FileUtils.touch(outputFile);
            InputStream inputStream = new FileInputStream(inputFile);
            byte[] bytes = AsmHelper.weaveSingleClassToByteArray(inputStream);
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(bytes);
            fos.close();
            inputStream.close();
        } else {
            if (inputFile.isFile()) {
                org.apache.commons.io.FileUtils.touch(outputFile);
                FileUtils.copyFile(inputFile, outputFile);
            }
        }
    }
}
