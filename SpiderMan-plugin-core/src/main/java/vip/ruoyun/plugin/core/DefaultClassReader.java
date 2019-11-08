package vip.ruoyun.plugin.core;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 * IClassReader 的默认实现
 * 功能：原文件 复制，不对 class 修改
 */
public class DefaultClassReader implements IClassReader {

    @Override
    public byte[] readSingleClassToByteArray(final InputStream inputStream) throws IOException {
        return IOUtils.toByteArray(inputStream);
    }

    /**
     * 全部不用读取
     */
    @Override
    public boolean canReadableClass(final String fullQualifiedClassName) {
        return false;
    }
}
