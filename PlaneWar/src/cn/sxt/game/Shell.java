package cn.sxt.game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
/**
 * 子弹类
 * 1.子弹的基本属性
 * 2.实现子弹的移动
 * 3.攻击飞机的功能(单个或者整个容器)
 * @author 闫旭杰
 */
public class Shell extends GameObject{
	//加载子弹图片
	Image shellImg = GameUtil.getImage("images/shell.png");
	static final float SHELL_SPEED=6;
	//从飞机类中获得的方向
	Plane.Direction dir;
	private boolean live = true;
	private boolean good; 
	private MyGameFrame gf;
	/**
	 * 
	 * @param x		初始X坐标
	 * @param y		初始Y坐标
	 * @param good	子弹的好坏属性
	 * @param dir	子弹方向
	 * @param gf	传入一个主类MyGameFrame的引用
	 */
	public Shell(double x,double y,boolean good,Plane.Direction dir,MyGameFrame gf) {
		this.x=x;
		this.y=y;
		this.good=good;
		this.dir=dir;
		this.gf=gf;
		this.width=shellImg.getWidth(null);
		this.height=shellImg.getHeight(null);
	}
	//画出子弹
	public void drawSelf(Graphics g) {
		if(!live)return;
		g.drawImage(shellImg,(int)x,(int)y,null);
		move();
	}
	/**
	 * 子弹的移动方法
	 * 上下移动
	 * 每次移动speed大小的距离
	 * 如果出界直接死亡并且从子弹容器中移除
	 */
	void move(){
		switch (dir) { 
		case U:
			y-=SHELL_SPEED;
			break;
		case D:
			y+=SHELL_SPEED;
			break;
		}
		if(x<0||y<0||x>MyGameFrame.WIDE||y>MyGameFrame.HIGH) {
			live=false;
			gf.shells.remove(this);
		}
	}
	/**
	 * 攻击飞机的方法
	 * @param plane	传进一个飞机对象
	 * @return	返回是否碰撞
	 */
	public boolean hitPlane(Plane plane) {
		//如果子弹活着并且子弹和飞机碰撞并且飞机活着并且子弹和飞机good属性相反
		if(this.live&&this.getRect().intersects(plane.getRect())&&plane.live&&this.good!=plane.isGood()) {
			if(plane.isGood()) {
				plane.setHp(plane.getHp()-20);
				if(plane.getHp()<=0) {
					plane.setLive(false);
				}
			}else {
				/*
				 * 如果是敌方飞机则直接死亡
				 * 分数增加10
				 * 50分的时候产生一个加血道具
				 * 100分的时候产生一个护盾道具
				 * 如果分数能整除200产生一个加血道具
				 * 如果分数能整除350产生一个护盾道具
				 */
				plane.setLive(false);
				gf.setGrade(gf.getGrade()+10);
					if(gf.getGrade()%200==0||gf.getGrade()==50) {
						gf.bloodProps.add(new BloodProps());
					}
					if(gf.getGrade()%350==0||gf.getGrade()==100) {
						gf.shieldProps.add(new ShieldProps());
					}
				}
			/**
			 * 碰撞后子弹死亡并且从子弹容器中去除
			 * 在碰撞位置产生一个爆炸对象并放入爆炸容器中
			 */
			this.setLive(false);
			gf.shells.remove(this);
			Explode e = new Explode(x-plane.width, y-plane.height,gf);
			gf.explodes.add(e);
			return true;
		}
		return false;
	}
	/**
	 * 子弹和敌机容器碰撞
	 * 和单个飞机碰撞外加一个for循环遍历
	 * @param enemyPlanes
	 * @return	返回是否碰撞
	 */
	public boolean hitPlanes(List<DPlane> enemyPlanes) {
		for (int i = 0; i < enemyPlanes.size(); i++) {
			if(hitPlane(enemyPlanes.get(i))) {
				return true;
			}
		}
		return false;
	}
	//子弹是否存活的set is方法
	public void setLive(boolean live) {
		this.live = live;
	}
	public boolean isLive() {
		return live;
	}
}
