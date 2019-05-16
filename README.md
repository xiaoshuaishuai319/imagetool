[![作者](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-%E5%B0%8F%E5%B8%85%E4%B8%B6-7AD6FD.svg)](https://www.xsshome.cn/)
```
       ├── cn.xsshome.imagetool            //包名
       ├── convert                               
       │       └── ImageToChar             //图片转字符图片、文本方法  
       ├── util        
       │       └── AnimatedGifEncoder      //GIF所需工具类代码
       │       └── Base64Util              //图片数据转base64编码工具类     
       │       └── GifDecoder              //Gif图片处理工具类代码
       │       └── GifImageUtil            //GIF图片添加文字特效工具类代码  
       │       └── LZWEncoder              //图片处理所需工具类代码
       │       └── MergeImageUtil          //图片特效合并工具类代码
       │       └── NeuQuant                //图片处理所需工具类代码
       │       └── RotateImageUtil         //图片旋转工具类代码
       ├── zoom   
       └──     └── ImageHelper             //图片缩放工具类代码 
```

### 示例代码
```
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
```

### 图片旋转示例代码
```
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
```
### GIF图片加文字特效示例代码
```
    public static void main(String[] args) throws Exception {
        GifImageUtil gifImageUtil = new GifImageUtil();
        String imagesavePath  = "C:\\Users\\xiaoshuai\\Desktop";//图片保存路径
        String imagesaveName = String.valueOf(System.currentTimeMillis());//图片保存名称 不包含后缀名
        String image  = "C:\\Users\\xiaoshuai\\Desktop\\db.gif";//原始图片
        String result = gifImageUtil.gifAddText(imagesavePath,imagesaveName,"微软雅黑",25,Color.pink,image,"图片添加文字","测试一下","1234","4567");
        System.out.println(result);
    }
```
### GIF图片加文字特效示例图
#### 原始图
![原始图](https://images.gitee.com/uploads/images/2019/0516/174359_5b52fe9a_131538.gif "原始图.gif")
#### 转换图
![转换图](https://images.gitee.com/uploads/images/2019/0516/174432_2af80fd2_131538.gif "转换图.gif")

