package cn.sxt.game;
import java.awt.Graphics;
import java.awt.Image;
import cn.sxt.game.GameUtil;
/**
 * 爆炸类
 * 1.初始化爆炸图片的数组
 * 2.画爆炸把爆炸数组里面的每个爆炸图片播放一遍
 */
public class Explode {
       double x,y;
       static Image[] imgs = new Image[32];
       private boolean live=true;
       int count=0;
       private MyGameFrame gf;
       public Explode(double x,double y,MyGameFrame gf){
           this.x = x;
           this.y = y;
           this.gf=gf; 
       }
       //初始化爆炸图片数组
       static {
               for(int i=0;i<32;i++){
                        imgs[i] = GameUtil.getImage("images/explode/"+(i)+".jpg");
                        imgs[i].getWidth(null);
               }
       }
       /*
        * 1.如果爆炸死亡则从爆炸容器中去除
        * 2.如果执行到最后一张图片则存活属性设置为false
        * 3.每执行一次然后执行下一张图片
        */
       public void draw(Graphics g){
    	   	if(!live) {
    	   		gf.explodes.remove(this);
    	   		return;
    	   	}
    	   	if(count==imgs.length) {
    	   		live=false;
    	   		count=0;
    	   		return;
    	   	}
    	   	g.drawImage(imgs[count], (int)x, (int)y,60,60, null);
    	   	count++;
       }
       public boolean isLive() {
       		return live;
       }
   		public void setLive(boolean live) {
   			this.live = live;
   		}
}