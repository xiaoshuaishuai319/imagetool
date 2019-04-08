package cn.xsshome.imagetool.util;

import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
/**
 * @ClassName: RotateImageUtil
 * @description: 图片旋转工具类
 * @author: 小帅丶
 * @create: 2019-04-08
 **/
public class RotateImageUtil {
    //全角度
    private static final int FULL_ANAGEL = 360;
    /**
     * 示例代码
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws  Exception {
        long start = System.currentTimeMillis();
        BufferedImage src = ImageIO.read(new File("E:\\testimg\\glassess.png"));
        BufferedImage des = rotateImage(src,20);
        ImageIO.write(des, "png", new File("E:\\testimg\\glassess2.png"));
        long end = System.currentTimeMillis();
        System.out.println("开始时间:" + start+ "; 结束时间:" + end+ "; 总共用时:" + (end - start) + "(ms)");
    }

    /**
     * 将图片进行指定角度旋转
     * @param bufferedImage 原图
     * @param angel 旋转角度
     * @return BufferedImage 旋转后的图片
     */
    public static BufferedImage rotateImage(BufferedImage bufferedImage, int angel) {
        if (bufferedImage == null) {
            return null;
        }
        if (angel < 0) {
            // 将负数角度，纠正为正数角度
            angel = angel + FULL_ANAGEL;
        }
        int imageWidth = bufferedImage.getWidth(null);
        int imageHeight = bufferedImage.getHeight(null);
        // 计算重新绘制图片的尺寸
        Rectangle rectangle = calculatorRotatedSize(new Rectangle(new Dimension(imageWidth, imageHeight)), angel);
        // 获取原始图片的透明度
        int type = bufferedImage.getColorModel().getTransparency();
        BufferedImage newImage = null;
        newImage = new BufferedImage(rectangle.width, rectangle.height, type);
        Graphics2D graphics = newImage.createGraphics();
        // 平移位置
        graphics.translate((rectangle.width - imageWidth) / 2, (rectangle.height - imageHeight) / 2);
        // 旋转角度
        graphics.rotate(Math.toRadians(angel), imageWidth / 2, imageHeight / 2);
        // 绘图
        graphics.drawImage(bufferedImage, null, null);
        return newImage;
    }
    /**
     * 计算旋转后的尺寸
     * @param src
     * @param angel
     * @return
     */
    private static Rectangle calculatorRotatedSize(Rectangle src, int angel) {
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }
        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new java.awt.Rectangle(new Dimension(des_width, des_height));
    }
}
