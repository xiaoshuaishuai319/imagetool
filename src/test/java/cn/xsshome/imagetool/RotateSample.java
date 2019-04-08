package cn.xsshome.imagetool;

import cn.xsshome.imagetool.util.RotateImageUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @ClassName: RotateSample
 * @description: 图片旋转示例代码
 * @author: 小帅丶
 * @create: 2019-04-08
 **/
public class RotateSample {
    public static void main(String[] args) throws  Exception {
        long start = System.currentTimeMillis();
        BufferedImage src = ImageIO.read(new File("E:\\testimg\\glassess.png"));//原图片本地路径
        BufferedImage des = RotateImageUtil.rotateImage(src,20);//旋转的角度
        ImageIO.write(des, "png", new File("E:\\testimg\\glassess2.png"));//旋转后保存的图片
        long end = System.currentTimeMillis();
        System.out.println("开始时间:" + start+ "; 结束时间:" + end+ "; 总共用时:" + (end - start) + "(ms)");
    }
}
