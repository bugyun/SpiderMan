package vip.ruoyun.plugin.core;

import java.io.IOException;
import java.io.InputStream;

public interface IAsmReader {

    /**
     * 读取 class 文件
     */
    byte[] readSingleClassToByteArray(InputStream inputStream) throws IOException;

    /**
     * 是否可读
     */
    boolean canReadableClass(String fullQualifiedClassName);

}
