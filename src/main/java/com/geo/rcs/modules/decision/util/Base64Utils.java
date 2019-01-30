package com.geo.rcs.modules.decision.util;


import java.io.*;

import org.reflections.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Base64和Image转换
 * @Author ZhengXingWang
 * @Email zhengxingwang@geotmt.com
 * @Date 2018/11/4  19:44
 **/
public class Base64Utils {
    private static final Logger log = LoggerFactory.getLogger(Base64Utils.class);

    public static void main(String[] args) throws Exception {

        //本地图片地址

        String url = "C:\\Users\\geo\\Desktop\\b.jpg";

        String str = Base64Utils.ImageToBase64ByLocal(url);

        System.out.println(str);

        Base64Utils.Base64ToImage(str, "C:\\Users\\geo\\Desktop\\report2.jpg");
    }

    /**
     * 将Image转为String，并且对其进行Base64编码
     * @param imgPath
     * @return
     */
    public static String ImageToBase64ByLocal(String imgPath) {

        InputStream in = null;
        byte[] data = null;

        // 读取图片字节数组
        try {
            in = new FileInputStream(imgPath);

            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();

        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }


    /**
     * Base64字符串转image
     * @param imgStr
     * @param imgPath
     * @return
     */
    public static boolean Base64ToImage(String imgStr,String imgPath) { // 对字节数组字符串进行Base64解码并生成图片
        boolean flag = true;
        if (Utils.isEmpty(imgStr)){ //数据为空
            flag = false;
            return flag;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream out = null;
        try{
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; i++){
                if (b[i] < 0){  //调整异常数据
                    b[i]+=256;
                }
            }
            File fileDir = new File(imgPath);
            if (!fileDir.exists()){
                boolean mkdirFlag = fileDir.mkdirs();
            }
            String imgFile = imgPath + "/result.png";
           File file = new File(imgFile);
            if (!file.exists()){
                file.createNewFile();
            }
            out =  new FileOutputStream(file);
            out.write(b);
            out.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
            log.error("本地生成图片失败");
            flag = false;
        }finally {
                try {
                    if (out != null){
                         out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return flag;
    }

}
