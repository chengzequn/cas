package com.czq.cas;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author chengzequn@foxmail.com
 * @since 2020/8/11 10:20
 */
public class CaptchaUtil {
    private static final String RANDOM_STARS="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String FONT_NAME = "Fixedsys";

    private static final int FONT_SIZE=18;

    private Random random=new Random();

    private int width=80;
    private int height=25;
    private int lineNum=50;
    private int strNum=4;

    /**
     * 生成验证码
     * @param randomCode
     * @return
     */
    public BufferedImage genRandomCodeImage(StringBuffer randomCode){
        //BufferedImage类是具有缓冲区的image类
        BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);
        //获取Graphics对象，便于对图像进行各种绘制操作
        Graphics g =image.getGraphics();
        //设置背景色
        g.setColor(getRandColor(200,250));
        g.fillRect(0,0,width,height);

        //设置干扰线颜色
        g.setColor(getRandColor(110,120));

        //绘制干扰线
        for(int i=0;i<=lineNum;i++){
            drowLine(g);
        }

        //绘制随机字符
        g.setFont(new Font(FONT_NAME,Font.ROMAN_BASELINE,FONT_SIZE));
        for(int i=1;i<=strNum;i++){
            randomCode.append(drowString(g,i));
        }
        g.dispose();
        return image;
    }

    /**
     * 给定范围的随机颜色
     */
    private Color getRandColor(int start,int end){
        if(start>255){
            start =255;
        }
        if (end>255){
            end=255;
        }
        int r=start+random.nextInt(end-start);
        int g=start+random.nextInt(end-start);
        int b=start+random.nextInt(end-start);
        return new Color(r,g,b);
    }

    /**
     * 绘制字符串
     */
    private String drowString(Graphics g,int i){
        g.setColor(new Color(random.nextInt(101),random.nextInt(111),random.nextInt(121)));
        String rand=String.valueOf(getRandomString(random.nextInt(RANDOM_STARS.length())));
        g.translate(random.nextInt(3),random.nextInt(3));
        g.drawString(rand,13*i,16);
        return rand;
    }

    /**
     * 绘制干扰线
     */
    private void drowLine(Graphics g){
        int xStart=random.nextInt(width);
        int yStart=random.nextInt(height);
        int xEnd=random.nextInt(16);
        int yEnd=random.nextInt(16);
        g.drawLine(xStart,yStart,xEnd,yEnd);
    }

    /**
     * 获取随机字符
     */
    private String getRandomString(int num){
        return String.valueOf(RANDOM_STARS.charAt(num));
    }
}
