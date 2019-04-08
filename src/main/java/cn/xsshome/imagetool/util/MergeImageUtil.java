package cn.xsshome.imagetool.util;

import cn.xsshome.imagetool.zoom.ImageHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @ClassName: MergeImageUtil
 * @description: 图片融合(两张图片合并为一张图片)
 * @author: 小帅丶
 * @create: 2019-04-08
 **/
public class MergeImageUtil {
    /**
     * 两张图片合并为一张图片
     * @param srcImage 底图
     * @param pendantImage 挂件图
     * @param x 距离右下角的X偏移量
     * @param y 距离右下角的Y偏移量
     * @param alpha  透明度, 选择值从0.0~1.0: 完全透明~完全不透明
     * @return BufferedImage
     * @throws Exception
     */
    public  static BufferedImage mergePendant(BufferedImage srcImage,BufferedImage pendantImage,int x,int y, float alpha)throws  Exception{
        //创建Graphics2D对象 用在挂件图像对象上绘图
        Graphics2D g2d = srcImage.createGraphics();
        //获取挂件图像的宽高
        int pendantImageWidth = pendantImage.getWidth();
        int pendantImageHeight = pendantImage.getHeight();
        //实现混合和透明效果
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,alpha));
        //绘制图像
        g2d.drawImage(pendantImage,x,y,pendantImageWidth,pendantImageWidth,null);
        return  srcImage;
    }
}
