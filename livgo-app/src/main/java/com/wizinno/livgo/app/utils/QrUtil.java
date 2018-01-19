package com.wizinno.livgo.app.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;

/**
 * Created by Administrator on 2017/7/19.
 */
public class QrUtil {
    public static String qrcode(String text,String username) throws IOException {
        System.out.println("hello world");
        int width = 590;
        int height = 590;
        String format = "png";
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        //直接写入文件
//        String url="D:\\apache-tomcat-7.0.72\\webapps\\livgo-server\\static\\img\\"+username+".png";
//        String url="http://localhost:8090/livgo-server/static/img/"+username+".png";
          String url="/usr/local/tomcat/webapps/livgo-server/static/img/"+username+".png";
        File outputFile = new File(url);
        MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
        String httpurl="http://server.livgo.com.cn/static/img/"+username+".png";
         return httpurl;
    }
}
