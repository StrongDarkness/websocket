package cn.qxl.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * 图片验证码生成器
 * Created by qiu on 2019/2/14.
 */
public class ImageCodeGenerator {

    // 验证码字符集
    private static final char[] chars = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    // 字符数量
    private static final int SIZE = 4;
    // 干扰线数量
    private static final int LINES = 5;
    // 宽度
    private static final int WIDTH = 100;
    // 高度
    private static final int HEIGHT = 40;

    /**
     * 生成随机字符串
     *
     * @param size
     * @return
     */
    public static String getRandomChar(int size) {
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int p = random.nextInt(chars.length);
            buffer.append(chars[p]);
        }
        return buffer.toString();
    }

    /**
     * 生成随机字符串
     *
     * @return
     */
    public static String getRandomChar() {
        return getRandomChar(SIZE);
    }

    /**
     * 随机取色
     */
    public static Color getRandomColor() {
        Random ran = new Random();
        Color color = new Color(ran.nextInt(256),
                ran.nextInt(256), ran.nextInt(256));
        return color;
    }

    /**
     * 生成图片
     *
     * @return
     */
    public static BufferedImage createImage(String chars) {
        return createImage(chars, WIDTH, HEIGHT, LINES);
    }

    /**
     * 生成图片
     *
     * @param chars
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage createImage(String chars, int width, int height) {
        return createImage(chars, width, height, LINES);
    }


    /**
     * 生成图片
     *
     * @param width
     * @param height
     * @param lines
     * @return
     */
    public static BufferedImage createImage(String chars, int width, int height, int lines) {
        if (width < WIDTH) width = WIDTH;
        if (height < HEIGHT) height = HEIGHT;
        if (lines < LINES) lines = LINES;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //获取图形上下文
        Graphics g = image.getGraphics();
        //设置背景色
//        g.setColor(Color.decode("#DCDCDC"));
        g.setColor(Color.WHITE);
        //画图范围
        g.fillRect(0, 0, width, height);
        Random random = new Random();
        //生成干扰线
        for (int i = 0; i < lines; i++) {
            g.setColor(getRandomColor());
            g.setFont(new Font("Times New Roman", Font.PLAIN, 24));
            int x0 = random.nextInt(width);
            int y0 = random.nextInt(height);
            int x1 = random.nextInt(15);
            int y1 = random.nextInt(15);
            g.drawLine(x0, y0, x0 + x1, y0 + y1);
        }
        //生成噪点
        float yawpRate = 0.03f;// 噪声率
        int area = (int) (yawpRate * width * height);//噪点数量
        for (int i = 0; i < area; i++) {
            int xxx = random.nextInt(width);
            int yyy = random.nextInt(height);
            int rgb = getRandomColor().getRGB();
            image.setRGB(xxx, yyy, rgb);
        }
        int fontSize = width / chars.length();
        if (fontSize < 24) fontSize = 24;
        //设置字体
        g.setFont(new Font("Times New Roman", Font.PLAIN, fontSize));
        //绘制字符串
        for (int i = 0; i < chars.length(); i++) {
            g.setColor(getRandomColor());
            String str = chars.substring(i, i + 1);
            int fy = (int) (-Math.random() * 10 + 6);
//            System.out.println(fy);
            g.drawString(str, i * width / chars.length(), height * 2 / 3 + fy);//依据宽度浮动
        }
        //释放图形上下文
        g.dispose();
        return image;
    }

    public static void main(String[] args) {
//        System.out.println(getRandomChar(6));
        try {
            String chars = getRandomChar(4);
            System.out.println(chars);
            BufferedImage image = createImage(chars, 100, 40);
            File file = new File("d:/test.jpg");
            ImageIO.write(image, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
