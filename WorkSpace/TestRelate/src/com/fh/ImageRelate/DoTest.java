package com.fh.ImageRelate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DoTest {

    public static void main(String[] args) throws IOException {

        //System.out.println(getDifferent(getHashCode("/home/csh/桌面/webwxgetmsgimg.jpg"),getHashCode("/home/csh/桌面/webwxgetmsgimg1.jpg")));
        System.out.println(getHashCode("/home/csh/桌面/webwxgetmsgimg.jpg"));
        System.out.println(getHashCode("/home/csh/桌面/webwxgetmsgimg1.jpg"));

    }

    public static int rgbToGray(int pixels) {
        // int _alpha =(pixels >> 24) & 0xFF;
        int _red = (pixels >> 16) & 0xFF;
        int _green = (pixels >> 8) & 0xFF;
        int _blue = (pixels) & 0xFF;
        return (int) (0.3 * _red + 0.59 * _green + 0.11 * _blue);
    }

    public static String binaryToHex(int binary){
        if (binary>9){
            switch (binary){
                case 10:return "a";
                case 11:return "b";
                case 12:return "c";
                case 13:return "d";
                case 14:return "e";
                default : return "f";
            }
        }else {
            return binary+"";
        }
    }

    public static String getHashCode(String filePath) throws IOException {
        File image = new File(filePath);//"/home/csh/桌面/webwxgetmsgimg.jpg"
        BufferedImage bufferedImage = ImageIO.read(image);

        int width = 8;
        int height = 8;

        int type = bufferedImage.getType();
        BufferedImage thumbImage = null;
        double sx = (double)width/bufferedImage.getWidth();
        double sy = (double)height/bufferedImage.getHeight();

        if (sx > sy){
            sx = sy;
            width = (int) (sx*bufferedImage.getWidth());
        }else{
            sy = sx;
            height = (int) (sy*bufferedImage.getHeight());
        }
        thumbImage = new BufferedImage(width,height,type);

        Graphics2D g =  bufferedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRenderedImage(bufferedImage, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();

        int[]pixels = new int[width * height];
        for (int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                pixels[i* height + j] = rgbToGray(bufferedImage.getRGB(i, j));
            }
        }

        int avgPixel= 0;
        int m = 0;
        for (int i =0; i < pixels.length; ++i) {
            m +=pixels[i];
        }
        m = m /pixels.length;
        avgPixel = m;

        int[] comps= new int[width * height];
        for (int i = 0; i < comps.length; i++) {
            if(pixels[i] >= avgPixel) {
                comps[i]= 1;
            }else {
                comps[i]= 0;
            }
        }

        StringBuffer hashCode = new StringBuffer();
        for (int i = 0; i < comps.length; i+= 4) {
            int result = comps[i] * (int) Math.pow(2, 3) + comps[i + 1] * (int) Math.pow(2, 2) + comps[i + 2] * (int) Math.pow(2, 1) + comps[i + 2];
            hashCode.append(binaryToHex(result));//二进制转为16进制
        }
        String sourceHashCode = hashCode.toString();

        return sourceHashCode;
    }

    public static int getDifferent(String hashCode1,String hashCode2){

        char[] chs1 = hashCode1.toCharArray();
        char[] chs2 = hashCode2.toCharArray();

        int different = 0;
        for (int i = 0; i < chs1.length; i++) {
            if (chs1[i] != chs2[i]){
                different++;
            }
        }
        return different;
    }

}
