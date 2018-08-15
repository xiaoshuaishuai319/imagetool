package cn.xsshome.imagetool.convert;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.FileImageInputStream;

import sun.misc.BASE64Encoder;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;

import cn.xsshome.imagetool.util.AnimatedGifEncoder;
import cn.xsshome.imagetool.util.Base64Util;
import cn.xsshome.imagetool.zoom.ImageHelper;
/**
 * 图片转字符工具类
 * @author 小帅丶
 * 2018年8月14日
 */
public class ImageToChar {
	static String ascii = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\\\"^`'.";
	static String base = "@#&$%*o!;.";
    /** 图片类型  */
    private static final int IMAGE_TYPE = BufferedImage.TYPE_INT_RGB;
	public static char toChar(int g) {
		if (g <= 30) {
			return '#';
		} else if (g > 30 && g <= 60) {
			return '&';
		} else if (g > 60 && g <= 120) {
			return '$';
		} else if (g > 120 && g <= 150) {
			return '*';
		} else if (g > 150 && g <= 180) {
			return 'o';
		} else if (g > 180 && g <= 210) {
			return '!';
		} else if (g > 210 && g <= 240) {
			return ';';
		} else {
			return ' ';
		}
	}
	/**
	 * 图片转字符图片
	 * @param imagePath 图片路径 
	 * @param txtPath 文本存放路径
	 * @throws IOException
	 */
	public static void load(String imagePath, String txtPath)
			throws IOException {
		BufferedImage image = ImageHelper.resize(ImageIO.read(new File(imagePath)),150,150);
		load(image, txtPath);
	}
	/**
	 * 图片转字符图片
	 * @param bi BufferedImage图片 
	 * @param txtPath 文本存放路径
	 * @throws IOException
	 */
	public static void load(BufferedImage bi, String txtPath)
			throws IOException {
		try {
			int width = bi.getWidth();
			int height = bi.getHeight();
			boolean flag = false;
			String result = "";
			for (int i = 0; i < height; i += 2) {
				for (int j = 0; j < width; j++) {
					int pixel = bi.getRGB(j, i); // 下面三行代码将一个数字转换为RGB数字
					int red = (pixel & 0xff0000) >> 16;
					int green = (pixel & 0xff00) >> 8;
					int blue = (pixel & 0xff);
					float gray = 0.299f * red + 0.578f * green + 0.114f * blue;
					int index = Math.round(gray * (base.length() + 1) / 255);
				    result += index >= base.length() ? " " : String.valueOf(base.charAt(index));
				}
				result += "\r\n";
			}
			flag = writeTxtFile(result,txtPath);
			System.out.println(flag?"图片转字符保存成功":"图片转字符保存失败");
		} catch (Exception e) {
			System.out.println("图片转字符异常"+e.getMessage());
		}
	}
	/**
	 * 字符保存到txt文件中 
	 * @param imageStr 字符
	 * @param txtPath  txt文件
	 * @return boolean
	 * @throws Exception
	 */
	private static boolean writeTxtFile(String imageStr, String txtPath) throws Exception{
		// 先读取原有文件内容，然后进行写入操作
		boolean flag = false;
		String filein = imageStr;
		String temp = "";
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		FileOutputStream fos = null;
		PrintWriter pw = null;
		try {
			// 文件路径
			File file = new File(txtPath);
			if (!file.exists()) {
				file.createNewFile();
			}
			// 将文件读入输入流
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			StringBuffer buf = new StringBuffer();
			// 保存该文件原有的内容
			for (int j = 1; (temp = br.readLine()) != null; j++) {
				buf = buf.append(temp);
			}
			buf.append(filein);
			fos = new FileOutputStream(file);
			pw = new PrintWriter(fos);
			pw.write(buf.toString().toCharArray());
			pw.flush();
			flag = true;
		} catch (IOException e) {
			System.out.println("文件保存失败"+e.getMessage());
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (fos != null) {
				fos.close();
			}
			if (br != null) {
				br.close();
			}
			if (isr != null) {
				isr.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
		return flag;
	}
	/**
	 * gif图片转gif字符图
	 * @param imagePath 原图路径
	 * @param outPath  输出图片的文件夹路径
	 * @throws IOException
	 */
	public static void loadGif(String imagePath, String outPath)
			throws IOException {
		File imageFile = new File(imagePath);
		FileImageInputStream in = new FileImageInputStream(imageFile);
		ImageReaderSpi readerSpi = new GIFImageReaderSpi();
		GIFImageReader gifImageReader = new GIFImageReader(readerSpi);
		gifImageReader.setInput(in);
		int num = gifImageReader.getNumImages(true);
		System.out.println(num);
		BufferedImage[] bufferedImages = new BufferedImage[num];
		for (int i = 0; i < num; i++) {
			BufferedImage bi = gifImageReader.read(i);
			bufferedImages[i] = txtToImage(bi, outPath + "out" + i + ".jpeg"); //每一帧都保存为图片
		}
		jpgToGif(bufferedImages,outPath + imagePath.substring(imagePath.length() - 6)+ "outGif.gif", 200);
	}
	/**
	 * 图片转字符再保存为图片
	 * @param bi 原图
	 * @param outPutPath
	 * @return BufferedImage
	 */
	public static BufferedImage txtToImage(BufferedImage bi, String outPutPath) {
		File imageFile = new File(outPutPath);
		if (!imageFile.exists()) {
			try {
				imageFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		int width = bi.getWidth();
		int height = bi.getHeight();
		int minx = bi.getMinX();
		int miny = bi.getMinY();
		System.out.println(width + " " + height);
		int speed = 7;
		BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		// 获取图像上下文
		Graphics g = createGraphics(bufferedImage, width, height, speed);
		// 图片中文本行高
		final int Y_LINEHEIGHT = speed;
		int lineNum = 1;
		for (int i = miny; i < height; i += speed) {
			for (int j = minx; j < width; j += speed) {
				int pixel = bi.getRGB(j, i); // 下面三行代码将一个数字转换为RGB数字
				int red = (pixel & 0xff0000) >> 16;
				int green = (pixel & 0xff00) >> 8;
				int blue = (pixel & 0xff);
				float gray = 0.299f * red + 0.578f * green + 0.114f * blue;
				int index = Math.round(gray * (base.length() + 1) / 255);
//				char c = ascii.charAt((int) (gray / 255 * ascii.length()));
//				char c = toChar((int) gray);
//				char c = toChar(index);
				String c = index >= base.length() ? " " : String.valueOf(base.charAt(index));
				g.drawString(String.valueOf(c), j, i);
			}
			lineNum++;
		}
		g.dispose();
		// 保存为jpg图片
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(imageFile);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
			OutputStream out = encoder.getOutputStream();
		    BASE64Encoder base64Encoder = new BASE64Encoder();
			encoder.encode(bufferedImage);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ImageFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bufferedImage;

	}
	/**
	 * 图片转字符再保存为图片 只返回图片的base64
	 * @param bi 原图
	 * @param outPutPath
	 * @return String
	 */
	public static String txtToImageByBase64(BufferedImage bi) {
		System.out.println("进来的时间"+System.currentTimeMillis());
		int width = bi.getWidth();
		int height = bi.getHeight();
		int minx = bi.getMinX();
		int miny = bi.getMinY();
		int speed = 7;
		BufferedImage bufferedImage = new BufferedImage(width,height,IMAGE_TYPE);
		// 获取图像上下文
		Graphics g = createGraphics(bufferedImage, width, height, speed);
		// 图片中文本行高
		final int Y_LINEHEIGHT = speed;
		int lineNum = 1;
		for (int i = miny; i < height; i += speed) {
			for (int j = minx; j < width; j += speed) {
				int pixel = bi.getRGB(j, i); // 下面三行代码将一个数字转换为RGB数字
				int red = (pixel & 0xff0000) >> 16;
				int green = (pixel & 0xff00) >> 8;
				int blue = (pixel & 0xff);
				float gray = 0.299f * red + 0.578f * green + 0.114f * blue;
				int index = Math.round(gray * (base.length() + 1) / 255);
				String c = index >= base.length() ? " " : String.valueOf(base.charAt(index));
				g.drawString(String.valueOf(c), j, i);
			}
			lineNum++;
		}
		g.dispose();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
	    try {
			ImageIO.write(bufferedImage,"jpg",out);
	        String base64 =Base64Util.encode(out.toByteArray());
			return base64;
		} catch (IOException e) {
			System.out.println("ImageIO.write异常" + e.getMessage());
		}finally{
			if(null!=out){
				try {
					out.close();
				} catch (IOException e) {
					System.out.println("out.close()异常" + e.getMessage());
				}
			}
		}
	    return null;
	}
	/**
	 * n张jpg转gif方法  
	 * @param bufferedImages
	 * @param newPic
	 * @param playTime
	 */
	private static void jpgToGif(BufferedImage[] bufferedImages, String newPic,
			int playTime) {
		try {
			AnimatedGifEncoder e = new AnimatedGifEncoder();
			e.setRepeat(0);
			e.start(newPic);
			for (int i = 0; i < bufferedImages.length; i++) {
				e.setDelay(playTime); // 设置播放的延迟时间
				e.addFrame(bufferedImages[i]); // 添加到帧中
			}
			e.finish();
		} catch (Exception e) {
			System.out.println("jpgToGif Failed:");
		}
	}
	/**
	 * 画板默认一些参数设置
	 * @param image 图片
	 * @param width 图片宽
 	 * @param height 图片高
	 * @param size 字体大小
	 * @return
	 */
	private static Graphics createGraphics(BufferedImage image, int width,
			int height, int size) {
		Graphics g = image.createGraphics();
		g.setColor(null); // 设置背景色
		g.fillRect(0, 0, width, height);// 绘制背景
		g.setColor(Color.BLACK); // 设置前景色
		g.setFont(new Font("微软雅黑", Font.PLAIN, size)); // 设置字体
		return g;
	}
}
