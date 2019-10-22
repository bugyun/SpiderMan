import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.print.DocFlavor.STRING;
import org.apache.commons.codec.binary.Base64;
import org.junit.*;

public class TestHash {

    @Test
    public void testHash() throws IOException {

        String str = "大胸弟 requestCode还在困扰你嘛";
        float len0 = str.length();
        long time = System.nanoTime();
        String ys = compress(str);
        long time1 = System.nanoTime();

        System.out.println(time1 - time);
        System.out.println("\n压缩后的字符串为----->" + ys);

        float len1 = ys.length();

        System.out.println("压缩后的字符串长度为----->" + len1);

        long time2 = System.nanoTime();
        String jy = unCompress(ys);
        long time3 = System.nanoTime();
        System.out.println(time3 - time2);
        System.out.println("\n解压缩后的字符串为--->" + jy);
        System.out.println("解压缩后的字符串长度为--->" + jy.length());

        System.out.println("\n压缩比例为" + len1 / len0);
    }

    /**
     * 字符串的压缩
     *
     * @param str 待压缩的字符串
     * @return 返回压缩后的字符串
     */
    public static String compress(String str) throws IOException {
        if (null == str || str.length() <= 0) {
            return str;
        }
        // 创建一个新的 byte 数组输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 使用默认缓冲区大小创建新的输出流
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        // 将 b.length 个字节写入此输出流
        gzip.write(str.getBytes());
        gzip.close();
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        return out.toString("UTF-8");
    }

    /**
     * 字符串的解压
     *
     * @param str 对字符串解压
     * @return 返回解压缩后的字符串
     */
    public static String unCompress(String str) throws IOException {
        if (null == str || str.length() <= 0) {
            return str;
        }
        // 创建一个新的 byte 数组输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组
        ByteArrayInputStream in = new ByteArrayInputStream(str
                .getBytes("ISO-8859-1"));
        // 使用默认缓冲区大小创建新的输入流
        GZIPInputStream gzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n = 0;
        while ((n = gzip.read(buffer)) >= 0) {// 将未压缩数据读入字节数组
            // 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte数组输出流
            out.write(buffer, 0, n);
        }
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        return out.toString("UTF-8");
    }


    @Test
    public void getMD5Str() throws Exception {
        StringBuilder hexString = new StringBuilder();
        String str = "大胸弟 requestCode还在困扰你嘛";
        // 生成一个MD5加密计算摘要
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 计算md5函数
        md.update(str.getBytes());
        byte[] hash = md.digest();
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
        System.out.println(hexString.toString());
    }

    @Test
    public void getUrl() throws IOException {
        String str = "大胸弟 requestCode还在困扰你嘛";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(str.getBytes());
        DataOutputStream dos = new DataOutputStream(baos);
        UUID uuid = UUID.randomUUID();
        dos.writeLong(uuid.getMostSignificantBits());
        String encoded = new String(Base64.encodeBase64(baos.toByteArray()), StandardCharsets.ISO_8859_1);
//        String shortUrlKey = StringUtils.left(encoded, 6); // returns the leftmost 6 characters
        System.out.println(encoded);
    }


}
