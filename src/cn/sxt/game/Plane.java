package cn.sxt.game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.*;
/**
 * 飞机类
 * 1.定义了飞机的主要属性
 * 2.实现了飞机使用键盘控制方向并且添加了一些按键的功能
 * 3.实现了飞机的攻击
 * 4.飞机和游戏道具的碰撞检测
 * 5.使用内部类显示血条
 * @author 闫旭杰
 *
 */
public class Plane extends GameObject{
	static final float PLANE_SPEED=4;
	//事先声明一个主窗口类的引用方便之后操纵主类属性
	MyGameFrame gf;
	//事先声明一个护盾类类的引用,触发护盾产生条件时接收对象
	Shield s;
	//生成随机数的对象
	Random rn = new Random();
	/*
	 * 事先声明时间对象
	 * 用来记录护盾的持续时间
	 * 方便之后用来做判断
	 */
	Date startTime;
	Date endTime;
	//飞机初始血量
	private int hp=200;
	//用来保存护盾持续时间
	public  int shieldSustaintime=0;
	//创建一个血条对象
	private BloodBar bb = new BloodBar(this);
	//标记飞机的好坏,默认为不属于DPlane
	private boolean good = !(this instanceof DPlane);
	//布尔变量标记飞机是否拥有护盾
	private boolean shield = false;
	//方向
	boolean left,up,right,down;
	//记录飞机生死
	boolean live=true;
	//枚举类型,保存了三个方向
	enum Direction {U,D,STOP}
	//默认方向为停止
	private Direction dir = Direction.STOP;
	/**
	 * 
	 * @param img 	传入飞机的图片
	 * @param x		飞机的x位置坐标
	 * @param y		飞机的y位置坐标
	 * @param dir	飞机的方向
	 */
	public Plane(Image img,double x,double y,Direction dir) {
		this.speed=(int)PLANE_SPEED;
		this.width=img.getWidth(null);
		this.height=img.getHeight(null);
		this.img=img;
		this.x=x;
		this.y=y;
		this.dir=dir;
	}
	/**
	 * 
	 * @param img	传入飞机的图片
	 * @param x		飞机的x位置坐标
	 * @param y		飞机的y位置坐标
	 * @param dir	飞机的方向
	 * @param gf	传入主类MyGameFrame的引用
	 */
	public Plane(Image img,double x,double y,Direction dir,MyGameFrame gf) {
		this(img,x,y,dir);
		this.speed=(int)PLANE_SPEED;
		this.width=img.getWidth(null);
		this.height=img.getHeight(null);
		this.gf=gf;
	}
	/**
	 * 画出自身的方法
	 */
	public void drawSelf(Graphics g) {
		//如果是我方战机则画出血条
		if(isGood()) bb.draw(g);
		/*
		 * 如果我方飞机死亡
		 * 提示按F2键复活
		 */
		if(!live&&isGood()) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			Font f=g.getFont();
			g.setFont(new Font("宋体",Font.BOLD,40));
			g.drawString("按F2键复活", 150, 250);
			g.setColor(c);
			g.setFont(f);
		}
		/*
		 * 如果活着画出飞机图片,并调用move方法
		 */
		if(live) {
			g.drawImage(img,(int)x,(int)y,null);
				move();
		 }
		/*
		 * 如果护盾存在则创建持续时间的对象
		 * 和开始时间相减则为护盾存在时间
		 * (开始时间对象创建于collidesWithShieldProps方法)
		 * 如果时间大于4秒则调用removeShield方法取消护盾
		 */
		if(shield) {
			//实时记录护盾的持续时间
			Date shieldSustainTime=new Date();
			shieldSustaintime=(int)((shieldSustainTime.getTime()-startTime.getTime())/1000);
			if(shieldSustaintime>4) {
				removeShield();
			}
		}
		/*
		 * 如果死亡并且属于DPlane
		 * 从敌方战机容器中去除
		 */
		 if(!live){
			 if(this instanceof DPlane) {
				 gf.enemyPlanes.remove(this);
			 }
			 return;
		 	}
		 /*
		  * 如果护盾不为空
		  * 则画出护盾
		  * (写在飞机的draw方法里就可以实现和飞机的同步)
		  */
		 if(s!=null) {
			 s.draw(g);
		 }
	}
	/**
	 * 飞机的移动方法
	 * 并且飞机不能出界
	 */
	public void move() {
			 if(left) {
				 x-=speed;
			 }
			 if(right) {
				 x+=speed;
			 }
			 if(up) {
				 y-=speed;
			 }
			 if(down) {
				 y+=speed;
			 }
			 if(x<0)x=0;
			 if(y<30)y=30;
			 if(x>MyGameFrame.WIDE-width)x=MyGameFrame.WIDE-width;
			 if(y>MyGameFrame.HIGH-height)y=MyGameFrame.HIGH-height;
	}
	//按下某个键增加相应的方向
	public void addDirection(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			left =true;
			break;
		case KeyEvent.VK_RIGHT:
			right =true;
			break;
		case KeyEvent.VK_UP:
			up =true;
			break;
		case KeyEvent.VK_DOWN:
			down =true;
			break;
		}
	}
	//松开某个键取消相应的方向
	public void minusDirection(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			left =false;
			break;
		case KeyEvent.VK_RIGHT:
			right =false;
			break;
		case KeyEvent.VK_UP:
			up =false;
			break;
		case KeyEvent.VK_DOWN:
			down =false;
			break;
		case KeyEvent.VK_X:
			fire();
			break;
		case KeyEvent.VK_H:
			addHp();
			break;
			/*
			 * 松开F2时重新开始
			 * 如果飞机死亡并且是我方飞机
			 * 飞机生命属性设置为true
			 * 生命值设为200
			 * 位置还原为初始位置
			 */
		case KeyEvent.VK_F2:
			if(!this.live&&this.good) {
				this.live=true;
				this.hp=200;
				x=230;
				y=450;
			}
			break;
			/*
			 * 松开P键时游戏暂停
			 * 再次点击P键游戏开始
			 */
		case KeyEvent.VK_P:
			if(!gf.p_stop)
			gf.p_stop=true;
			else gf.p_stop=false;
			break;
			/*
			 * 点击B键开始游戏
			 */
		case KeyEvent.VK_B:
			if(!gf.b_begin)
			gf.b_begin=true;
			break;
		}
	}
	/**
	 * 开火方法
	 * @return	返回一枚子弹
	 */
	public Shell fire() {
		//如果死了就不会开火
		if(!live) {
			return null;
		}
		//new出子弹并且添加到子弹容器中
		Shell s = new Shell(x+17,y-20,good,dir,gf);
		gf.shells.add(s);
		return s;
	}
	/**
	 * 飞机与飞机之间的碰撞检测
	 * @param p 传进来一个飞机对象
	 * @return  返回是否相撞
	 */
	public boolean collidesWithPlane(Plane p) {
		//如果飞机活着并且两个飞机返回的矩形相碰并且两个飞机的good属性不同(不是同一种飞机)
		if(this.live&&p.isLive()&&this.getRect().intersects(p.getRect())&&this.good!=p.isGood()) {
			//参数飞机死亡
			this.live=false;
			//调用此方法的飞机生命值减少40
			p.setHp(p.getHp()-40);
			//如果生命值小于0则死亡
			if(p.getHp()<=0)p.setLive(false);
			//创建一个爆炸对象并且加入到爆炸容器中
			Explode e = new Explode(x-p.width, y-p.height,gf);
			gf.explodes.add(e);
			//将死亡的敌方飞机从容器中移除
			gf.enemyPlanes.remove(this);
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param b 传入一个加血道具
	 * 加血道具的碰撞检测
	 * 如果是我方飞机则生命值+50
	 * 把加血道具的生存变量设为false
	 * 并且从加血道具的容器中去除
	 */
	public void collidesWithBlode(BloodProps b) {
		if(this.live&&b.isLife()&&this.getRect().intersects(b.getRect())&&this.good) {
			this.setHp(this.getHp()+50);
			b.setLife(false);
			gf.bloodProps.remove(b);
		}	
	}
	/**
	 * 护盾道具的碰撞检测
	 * @param sp	传入一个护盾道具
	 * @return		返回是否碰撞
	 * 护盾道具生命设为false
	 * 飞机护盾(shield)属性设为true
	 * 创建护盾产生计时对象
	 */
	public boolean collidesWithShieldProps(ShieldProps sp) {
		if(this.live&&sp.isLife()&&this.getRect().intersects(sp.getRect())&&this.good) {
			sp.setLife(false);
			gf.shieldProps.remove(sp);
			s=new Shield(this);
			shield=true;
			startTime = new Date();
			return true;
		}
		return false;
	}
	/*
	 * 去除护盾方法
	 * 如果护盾属性为true并且shield(飞机是否拥有护盾)属性为true
	 * 护盾对象存在属性和飞机护盾标志设为false
	 */
	public void removeShield() {
		if(shield==true&&s.live==true) {
		shield=false;
		s.live=false;
		}
	}
	/**
	 * 血条内部类
	 * @author 闫旭杰
	 *
	 */
	public class BloodBar {
		Plane p;
		/**
		 * 
		 * @param p		传入一个飞机对象
		 */
		BloodBar(Plane p){
			this.p=p;
		}
		public void draw (Graphics g) {
			Color c= g.getColor();
			g.setColor(Color.RED);
			//空心矩形作为背景
			g.drawRect(0, 20, 80, 20);
			int w = 80* p.getHp()/200;
			//实心矩形为血条
			g.fillRect(0, 20, w, 20);
			g.setColor(c);
		}
	}
	//加血方法
	public void addHp() {
		this.setHp(1000);
	}
	//飞机生命值的get set属性
	public int getHp() {
		return hp;
	}
	public void setHp(int life) {
		this.hp = life;
	}
	//飞机存活属性的get is方法
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean life) {
		this.live = life;
	}
	//获得飞机的good属性
	public boolean isGood() {
		return good;
	}
	public boolean isShield() {
		return shield;
	}
}
