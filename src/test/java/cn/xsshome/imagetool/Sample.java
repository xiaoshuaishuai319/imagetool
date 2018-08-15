package cn.xsshome.imagetool;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import cn.xsshome.imagetool.convert.ImageToChar;
/**
 * 调用示例
 * @author 小帅丶
 *
 */
public class Sample {
	public static void main(String[] args) throws Exception {
		ImageToChar.load("G:/phone.jpg", "F:/gif/woman.txt");//静态图片转字符保存为txt文件
		ImageToChar.loadGif("C:/Users/Administrator/Desktop/页面录屏显示.gif", "F:/gif/");//动图转为动态的字符图片
		BufferedImage bi = null;
		bi = ImageIO.read(new File("G:/body.jpg"));
        String bytePic = ImageToChar.txtToImageByBase64(bi);//静态图转字符 返回转换后图片的base64
        System.out.println(bytePic);
	}
}
