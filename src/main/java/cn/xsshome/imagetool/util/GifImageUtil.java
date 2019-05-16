package cn.xsshome.imagetool.util;

import sun.awt.SunHints;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

/**
 * @ClassName: GifImageUtil
 * @description: GIF图片加文字
 * @author: 小帅丶
 * @create: 2019-05-10
 **/
public class GifImageUtil {
    /**
     * @author 小帅丶
     * @date 2019/5/10
     * @param srcImgPath gif保存路径
     * @param imgName gif图片名称不包含后缀名
     * @param fontName 字体名称
     * @param fontSize 字体大小
     * @param color 字体颜色
     * @param gifImagePath 图片本地路径
     * @param markContent 要添加的文字内容 可多个
     * @return String
     **/
    public String gifAddText(String srcImgPath,String imgName,String fontName,int fontSize,Color color,String gifImagePath,String... markContent) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            String filePath = srcImgPath+File.separator+imgName+".gif";
            InputStream inputStream = new FileInputStream(gifImagePath);
            GifDecoder decoder = new GifDecoder();
            //读入gif数据流
            decoder.read(inputStream);
            //获取GIF的宽高
            Dimension dimension = decoder.getFrameSize();
            int height = (int)dimension.getHeight();
            int width = (int)dimension.getWidth();
            //生成字体
            Font font = new Font(fontName,Font.BOLD,fontSize);
            //读取帧数
            int frameCount = decoder.getFrameCount();
            AnimatedGifEncoder encoder = new AnimatedGifEncoder();
            String url = filePath;
            encoder.start(url);
            encoder.setRepeat(0);
            Graphics2D g = null;
            /**
             * 对GIF进行拆分
             * 每一帧进行文字处理
             * 组装
             */
            for (int i = 0; i < frameCount; i++) {
                //初始化图像
                g =  (Graphics2D) decoder.getFrame(i).getGraphics();
                /**
                 * RenderingHint是对图片像素，锯齿等等做的优化，可保证生成的图片放大锯齿点阵也不会很明显
                 */
                g.setRenderingHint(SunHints.KEY_ANTIALIASING, SunHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(SunHints.KEY_TEXT_ANTIALIASING, SunHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
                g.setColor(color);
                g.setFont(font);
                //文本内容转数组方便判断
                String markContents [] = markContent;
                //设置打印文字和坐标
                int length = markContent.length;
                int step = frameCount/length;
                //要放的所有帧数
                int steps [] = new int[length];

                //对每次执行的帧数进行赋值 eg: 文本3句话  则 总帧数/3 为每句话显示的帧步长
                for (int j = 0; j < markContents.length; j++) {
                    steps[j] = (j+1)*step;
                }
                //多个文字
                if(markContents.length>1){
                    //对文字进行填充
                    for (int k = 0; k < steps.length; k++) {
                        if(k==0){
                            if(0<=i&&i<=steps[k]){
                                g.drawString(markContent[k], width/3, height-20);
                            }
                        }
                        if(k>0&&k<steps.length-1) {
                            if (steps[k - 1] < i && i <= steps[k]) {
                                g.drawString(markContent[k], width/3, height-20);
                            }
                        }
                        if (k == steps.length - 1) {
                            if (i>steps[k-1]) {
                                g.drawString(markContent[k], width/3, height-20);
                            }
                        }
                    }
                }else{
                    g.drawString(markContent[0], width/3, height-20);
                }
                g.dispose();
                //组装每一帧
                encoder.addFrame(decoder.getFrame(i));
                //设置每帧的切换时间
                if (i != frameCount - 1) {
                    encoder.setDelay(decoder.getDelay(i));
                }
            }
            encoder.finish();
            return filePath;
        }finally {
            try {
                if (null != outputStream)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
