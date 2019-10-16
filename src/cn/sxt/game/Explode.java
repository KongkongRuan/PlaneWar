package cn.sxt.game;
import java.awt.Graphics;
import java.awt.Image;
import cn.sxt.game.GameUtil;
/**
 * ��ը��
 * 1.��ʼ����ըͼƬ������
 * 2.����ը�ѱ�ը���������ÿ����ըͼƬ����һ��
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
       //��ʼ����ըͼƬ����
       static {
               for(int i=0;i<32;i++){
                        imgs[i] = GameUtil.getImage("images/explode/"+(i)+".jpg");
                        imgs[i].getWidth(null);
               }
       }
       /*
        * 1.�����ը������ӱ�ը������ȥ��
        * 2.���ִ�е����һ��ͼƬ������������Ϊfalse
        * 3.ÿִ��һ��Ȼ��ִ����һ��ͼƬ
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