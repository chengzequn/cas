package com.czq.cas;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 获取验证码
 * @author chengzequn@foxmail.com
 * @since 2020/8/11 11:20
 */
@Controller
public class CaptchaController {

    public static final String KEY_CAPTCHA="captcha";

    @RequestMapping("/captcha.jpg")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response){
        //设置相应类型，告诉浏览器输出的内容为图片
        response.setContentType("image/jpeg");
        //不缓存此内容
        response.setHeader("Pragma","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expire",0);
        try {
            //生成验证码并放到session中
            HttpSession session=request.getSession();
            CaptchaUtil captchaUtil=new CaptchaUtil();
            StringBuffer buffer=new StringBuffer();
            BufferedImage image=captchaUtil.genRandomCodeImage(buffer);
            session.removeAttribute(KEY_CAPTCHA);
            session.setAttribute(KEY_CAPTCHA,buffer.toString());

            //将内存中的图片通过流动形式输出到客户端
            ImageIO.write(image,"JPEG",response.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
