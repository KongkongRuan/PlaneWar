package cn.sxt.game;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class GameUtil {
     
    private GameUtil() {// 工具类一般构造器私有。
         
    };
 /**
  * 返回指定路径的图片对象
  * @param path
  * @return
  */
    public static Image getImage(String path) {
        URL u = GameUtil.class.getClassLoader().getResource(path);
        BufferedImage img = null;
        try {
            img = ImageIO.read(u);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return img;
    }
 
}