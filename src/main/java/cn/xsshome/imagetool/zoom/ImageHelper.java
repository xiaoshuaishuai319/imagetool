package cn.xsshome.imagetool.zoom;

import java.awt.Graphics2D;  

import java.awt.Rectangle;  
import java.awt.RenderingHints;  
import java.awt.geom.AffineTransform;  
import java.awt.image.BufferedImage;  
import java.awt.image.ColorModel;  
import java.awt.image.WritableRaster;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.IOException;  
import java.io.InputStream;  
  
import javax.imageio.ImageIO;
/**
 * 图像缩放处理
 * @author 小帅丶
 *
 */
public class ImageHelper {
	 /**   
     * 实现图像的等比缩放   
     * @param source   
     * @param targetW   
     * @param targetH   
     * @return   
     */    
    public static BufferedImage resize(BufferedImage source, int targetW,     
            int targetH) {     
        // targetW，targetH分别表示目标长和宽     
        int type = source.getType();     
        BufferedImage target = null;     
        double sx = (double) targetW / source.getWidth();     
        double sy = (double) targetH / source.getHeight();     
        // 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放     
        // 则将下面的if else语句注释即可     
        if (sx < sy) {     
            sx = sy;     
            targetW = (int) (sx * source.getWidth());     
        } else {     
            sy = sx;     
            targetH = (int) (sy * source.getHeight());     
        }     
        if (type == BufferedImage.TYPE_CUSTOM) { // handmade     
            ColorModel cm = source.getColorModel();     
            WritableRaster raster = cm.createCompatibleWritableRaster(targetW,     
                    targetH);     
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();     
            target = new BufferedImage(cm, raster, alphaPremultiplied, null);     
        } else    
            target = new BufferedImage(targetW, targetH, type);     
        Graphics2D g = target.createGraphics();     
        // smoother than exlax:     
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,     
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);     
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));     
        g.dispose();     
        return target;     
    }     
   
    /**   
     * 实现图像的等比缩放和缩放后的截取, 处理成功返回true, 否则返回false   
     * @param inFilePath 要截取文件的路径   
     * @param outFilePath 截取后输出的路径   
     * @param width 要截取宽度   
     * @param hight 要截取的高度   
     * @throws Exception   
     */    
    public static boolean compress(String inFilePath, String outFilePath,     
            int width, int hight) {  
        boolean ret = false;  
        File file = new File(inFilePath);  
        File saveFile = new File(outFilePath);  
        InputStream in = null;  
        try {  
            in = new FileInputStream(file);  
            ret = compress(in, saveFile, width, hight);  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
            ret = false;  
        } finally{  
            if(null != in){  
                try {  
                    in.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
   
        return ret;  
    }   
   
    /**   
     * 实现图像的等比缩放和缩放后的截取, 处理成功返回true, 否则返回false   
     * @param in 要截取文件流 
     * @param outFilePath 截取后输出的路径   
     * @param width 要截取宽度   
     * @param hight 要截取的高度   
     * @throws Exception   
     */    
    public static boolean compress(InputStream in, File saveFile,     
            int width, int hight) {  
//     boolean ret = false;  
        BufferedImage srcImage = null;  
        try {  
            srcImage = ImageIO.read(in);  
        } catch (IOException e) {  
            e.printStackTrace();  
            return false;  
        }  
   
        if (width > 0 || hight > 0) {  
            // 原图的大小  
            int sw = srcImage.getWidth();  
            int sh = srcImage.getHeight();  
            // 如果原图像的大小小于要缩放的图像大小，直接将要缩放的图像复制过去  
            if (sw > width && sh > hight) {  
                srcImage = resize(srcImage, width, hight);  
            } else {  
                String fileName = saveFile.getName();  
                String formatName = fileName.substring(fileName  
                        .lastIndexOf('.') + 1);  
                try {  
                    ImageIO.write(srcImage, formatName, saveFile);  
                } catch (IOException e) {  
                    e.printStackTrace();  
                    return false;  
                }  
                return true;  
            }  
        }  
        // 缩放后的图像的宽和高  
        int w = srcImage.getWidth();  
        int h = srcImage.getHeight();  
        // 如果缩放后的图像和要求的图像宽度一样，就对缩放的图像的高度进行截取  
        if (w == width) {  
            // 计算X轴坐标  
            int x = 0;  
            int y = h / 2 - hight / 2;  
            try {  
                saveSubImage(srcImage, new Rectangle(x, y, width, hight), saveFile);  
            } catch (IOException e) {  
                e.printStackTrace();  
                return false;  
            }  
        }  
        // 否则如果是缩放后的图像的高度和要求的图像高度一样，就对缩放后的图像的宽度进行截取  
        else if (h == hight) {  
            // 计算X轴坐标  
            int x = w / 2 - width / 2;  
            int y = 0;  
            try {  
                saveSubImage(srcImage, new Rectangle(x, y, width, hight), saveFile);  
            } catch (IOException e) {  
                e.printStackTrace();  
                return false;                  
            }  
        }  
   
        return true;  
    }  
   
    /** 
     * 实现图像的等比缩放和缩放后的截取, 处理成功返回true, 否则返回false 
     * @param in 图片输入流 
     * @param saveFile 压缩后的图片输出流 
     * @param proportion 压缩比 
     * @throws Exception 
     */  
    public static boolean compress(InputStream in, File saveFile, int proportion) {  
        if(null == in  
                ||null == saveFile  
                ||proportion < 1){// 检查参数有效性  
            //LoggerUtil.error(ImageHelper.class, "--invalid parameter, do nothing!");  
            return false;  
        }  
   
        BufferedImage srcImage = null;  
        try {  
            srcImage = ImageIO.read(in);  
        } catch (IOException e) {  
            e.printStackTrace();  
            return false;  
        }  
        // 原图的大小  
        int width = srcImage.getWidth() / proportion;  
        int hight = srcImage.getHeight() / proportion;  
   
        srcImage = resize(srcImage, width, hight);  
   
        // 缩放后的图像的宽和高  
        int w = srcImage.getWidth();  
        int h = srcImage.getHeight();  
        // 如果缩放后的图像和要求的图像宽度一样，就对缩放的图像的高度进行截取  
        if (w == width) {  
            // 计算X轴坐标  
            int x = 0;  
            int y = h / 2 - hight / 2;  
            try {  
                saveSubImage(srcImage, new Rectangle(x, y, width, hight), saveFile);  
            } catch (IOException e) {  
                e.printStackTrace();  
                return false;  
            }  
        }  
        // 否则如果是缩放后的图像的高度和要求的图像高度一样，就对缩放后的图像的宽度进行截取  
        else if (h == hight) {  
            // 计算X轴坐标  
            int x = w / 2 - width / 2;  
            int y = 0;  
            try {  
                saveSubImage(srcImage, new Rectangle(x, y, width, hight), saveFile);  
            } catch (IOException e) {  
                e.printStackTrace();  
                return false;  
            }  
        }  
   
        return true;  
    }  
   
    /** 
     * 实现缩放后的截图 
     * @param image 缩放后的图像 
     * @param subImageBounds 要截取的子图的范围 
     * @param subImageFile 要保存的文件 
     * @throws IOException  
     */    
    private static void saveSubImage(BufferedImage image,     
            Rectangle subImageBounds, File subImageFile) throws IOException {     
        if (subImageBounds.x < 0 || subImageBounds.y < 0    
                || subImageBounds.width - subImageBounds.x > image.getWidth()     
                || subImageBounds.height - subImageBounds.y > image.getHeight()) {     
            //LoggerUtil.error(ImageHelper.class, "Bad subimage bounds");     
            return;     
        }     
        BufferedImage subImage = image.getSubimage(subImageBounds.x,subImageBounds.y, subImageBounds.width, subImageBounds.height);     
        String fileName = subImageFile.getName();     
        String formatName = fileName.substring(fileName.lastIndexOf('.') + 1);     
        ImageIO.write(subImage, formatName, subImageFile);  
    }     
      
      
   public static void main(String[] args) throws Exception {  
       /** 
        * saveSubImage 截图类的使用 
        * srcImage 为BufferedImage对象 
        * Rectangle    为需要截图的长方形坐标 
        * saveFile 需要保存的路径及名称 
        * **/  
        //需要截图的长方形坐标  
        /*Rectangle rect =new Rectangle(); 
        rect.x=40; 
        rect.y=40; 
        rect.height=160; 
        rect.width=160; 
  
        InputStream in = null; 
        //需要保存的路径及名称 
        File saveFile = new File("d:\\ioc\\files\\aaa2.jpg"); 
        //需要进行处理的图片的路径 
        in = new FileInputStream(new File("d:\\ioc\\files\\aaa.jpg")); 
        BufferedImage srcImage = null; 
        //将输入的数据转为BufferedImage对象 
        srcImage = ImageIO.read(in); 
  
        ImageHelper img=new ImageHelper(); 
        img.saveSubImage(srcImage, rect, saveFile);*/  
   
       /** 
        * compress 图片缩放类的使用(缩略图) 
        * srcImage 为InputStream对象 
        * Rectangle    为需要截图的长方形坐标 
        * proportion 为压缩比例 
        * **/  
        InputStream in = null;  
        //缩放后需要保存的路径  
        File saveFile = new File("d:\\ABC.jpg");  
        try {  
            //原图片的路径  
            in = new FileInputStream(new File("d:\\download.jpg"));  
            if(compress(in, saveFile, 5)){  
                System.out.println("图片压缩5倍！");  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            in.close();  
        }  
    }  
}
