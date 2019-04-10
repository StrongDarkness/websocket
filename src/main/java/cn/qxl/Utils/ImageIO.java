package cn.qxl.Utils;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * base64图片转换工具
 * Created by qiu on 2019/1/23.
 */
public class ImageIO {

    /**
     * base64生成图片
     *
     * @param imgBase64Str
     * @param path
     * @return
     */
    public static boolean generateImage(String imgBase64Str, String path) {
        if (imgBase64Str == null) {
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] bytes = decoder.decodeBuffer(imgBase64Str);
            for (int i = 0; i < bytes.length; i++) {
                if (bytes[i] < 0) {
                    bytes[i] += 256;
                }
            }
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream out = new FileOutputStream(path);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 生成base64字符串
     *
     * @param path
     * @return
     */
    public static String getImgBase64Str(String path) {
        if (path == null) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        try {
            is = new FileInputStream(path);
            data = new byte[is.available()];
            is.read(data);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    public static void main(String[] args) {
        String str = getImgBase64Str("E:\\images\\82296627.jpg");
        System.out.println(str);
        generateImage(str, "E:\\images\\82296628.jpg");
    }
}
