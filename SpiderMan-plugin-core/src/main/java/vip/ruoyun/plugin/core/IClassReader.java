package vip.ruoyun.plugin.core;

import java.io.InputStream;

public interface IClassReader {

    /**
     * 读取 class 文件
     */
    byte[] readSingleClassToByteArray(InputStream inputStream) throws Exception;

    /**
     * 是否可读
     */
    boolean canReadableClass(String fullQualifiedClassName);

}
