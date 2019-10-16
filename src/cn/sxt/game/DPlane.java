package cn.sxt.game;
import java.awt.Image;
/**
 * 敌方飞机类(并没有完全独立出来 继承了Plane类)
 * 1.拥有自己独有的构造方法
 * 2.自己独有的构造方法
 * 3.自己独有的开火方法
 * @author 闫旭杰
 */
public class DPlane extends Plane{
	static final float DPLANE_SPEED=3;
	public DPlane(Image img, double x, double y, Direction dir,MyGameFrame gf) {
		super(img, x, y, dir,gf);
	}
	/*每次移动距离1-3之间随机
	 * 如果出界直接死亡
	 * 1/59的几率发射子弹
	 * (non-Javadoc)
	 * @see cn.sxt.game.Plane#move()
	 */
	public void move(){
		y+=rn.nextInt(3)+1;
		if(y>MyGameFrame.HIGH) {
			setLive(false);
		}
		if(rn.nextInt(60)>58) {
			this.fire();
		}
	}
	/**
	 * 敌机开火方法
	 * 1.死了不会开火
	 * 2.子弹方向固定向下
	 * 3.把生成的子弹也添加到子弹容器中
	 */
	public Shell fire() {
		if(!live) {
			return null;
		}
		Shell s = new Shell(x+13,y+48,false,Plane.Direction.D,gf);
		gf.shells.add(s);
		return s;
	}
}
