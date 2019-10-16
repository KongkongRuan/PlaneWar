package cn.sxt.game;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

/**
 * 游戏物体根类
 * 所有的游戏物体都继承本类
 * @author KJ
 *
 */
public class GameObject {
	 Image img;
	 double x,y; 
	 int speed;
	 int width,height;
	 public void drawSelf(Graphics g) {
		 g.drawImage(img,(int)x,(int)y,null);
	 }
//下面三个全是构造方法

	 
	public GameObject(Image img, double x, double y, int speed, int width, int height) {
		super();
		this.img = img;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.width = width;
		this.height = height;
	}

	public GameObject(Image img, double x, double y) {
		super();
		this.img = img;
		this.x = x;
		this.y = y;
	}
	public GameObject() {
	}
	 
	/**
	 * 返回矩形 方便后面的碰撞检测
	 * @return
	 */
	public Rectangle getRect() {
		return new Rectangle((int)x, (int)y, width, height);
	}
}
