package cn.sxt.game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.*;
/**
 * �ɻ���
 * 1.�����˷ɻ�����Ҫ����
 * 2.ʵ���˷ɻ�ʹ�ü��̿��Ʒ����������һЩ�����Ĺ���
 * 3.ʵ���˷ɻ��Ĺ���
 * 4.�ɻ�����Ϸ���ߵ���ײ���
 * 5.ʹ���ڲ�����ʾѪ��
 * @author �����
 *
 */
public class Plane extends GameObject{
	static final float PLANE_SPEED=4;
	//��������һ��������������÷���֮�������������
	MyGameFrame gf;
	//��������һ���������������,�������ܲ�������ʱ���ն���
	Shield s;
	//����������Ķ���
	Random rn = new Random();
	/*
	 * ��������ʱ�����
	 * ������¼���ܵĳ���ʱ��
	 * ����֮���������ж�
	 */
	Date startTime;
	Date endTime;
	//�ɻ���ʼѪ��
	private int hp=200;
	//�������滤�ܳ���ʱ��
	public  int shieldSustaintime=0;
	//����һ��Ѫ������
	private BloodBar bb = new BloodBar(this);
	//��Ƿɻ��ĺû�,Ĭ��Ϊ������DPlane
	private boolean good = !(this instanceof DPlane);
	//����������Ƿɻ��Ƿ�ӵ�л���
	private boolean shield = false;
	//����
	boolean left,up,right,down;
	//��¼�ɻ�����
	boolean live=true;
	//ö������,��������������
	enum Direction {U,D,STOP}
	//Ĭ�Ϸ���Ϊֹͣ
	private Direction dir = Direction.STOP;
	/**
	 * 
	 * @param img 	����ɻ���ͼƬ
	 * @param x		�ɻ���xλ������
	 * @param y		�ɻ���yλ������
	 * @param dir	�ɻ��ķ���
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
	 * @param img	����ɻ���ͼƬ
	 * @param x		�ɻ���xλ������
	 * @param y		�ɻ���yλ������
	 * @param dir	�ɻ��ķ���
	 * @param gf	��������MyGameFrame������
	 */
	public Plane(Image img,double x,double y,Direction dir,MyGameFrame gf) {
		this(img,x,y,dir);
		this.speed=(int)PLANE_SPEED;
		this.width=img.getWidth(null);
		this.height=img.getHeight(null);
		this.gf=gf;
	}
	/**
	 * ��������ķ���
	 */
	public void drawSelf(Graphics g) {
		//������ҷ�ս���򻭳�Ѫ��
		if(isGood()) bb.draw(g);
		/*
		 * ����ҷ��ɻ�����
		 * ��ʾ��F2������
		 */
		if(!live&&isGood()) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			Font f=g.getFont();
			g.setFont(new Font("����",Font.BOLD,40));
			g.drawString("��F2������", 150, 250);
			g.setColor(c);
			g.setFont(f);
		}
		/*
		 * ������Ż����ɻ�ͼƬ,������move����
		 */
		if(live) {
			g.drawImage(img,(int)x,(int)y,null);
				move();
		 }
		/*
		 * ������ܴ����򴴽�����ʱ��Ķ���
		 * �Ϳ�ʼʱ�������Ϊ���ܴ���ʱ��
		 * (��ʼʱ����󴴽���collidesWithShieldProps����)
		 * ���ʱ�����4�������removeShield����ȡ������
		 */
		if(shield) {
			//ʵʱ��¼���ܵĳ���ʱ��
			Date shieldSustainTime=new Date();
			shieldSustaintime=(int)((shieldSustainTime.getTime()-startTime.getTime())/1000);
			if(shieldSustaintime>4) {
				removeShield();
			}
		}
		/*
		 * ���������������DPlane
		 * �ӵз�ս��������ȥ��
		 */
		 if(!live){
			 if(this instanceof DPlane) {
				 gf.enemyPlanes.remove(this);
			 }
			 return;
		 	}
		 /*
		  * ������ܲ�Ϊ��
		  * �򻭳�����
		  * (д�ڷɻ���draw������Ϳ���ʵ�ֺͷɻ���ͬ��)
		  */
		 if(s!=null) {
			 s.draw(g);
		 }
	}
	/**
	 * �ɻ����ƶ�����
	 * ���ҷɻ����ܳ���
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
	//����ĳ����������Ӧ�ķ���
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
	//�ɿ�ĳ����ȡ����Ӧ�ķ���
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
			 * �ɿ�F2ʱ���¿�ʼ
			 * ����ɻ������������ҷ��ɻ�
			 * �ɻ�������������Ϊtrue
			 * ����ֵ��Ϊ200
			 * λ�û�ԭΪ��ʼλ��
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
			 * �ɿ�P��ʱ��Ϸ��ͣ
			 * �ٴε��P����Ϸ��ʼ
			 */
		case KeyEvent.VK_P:
			if(!gf.p_stop)
			gf.p_stop=true;
			else gf.p_stop=false;
			break;
			/*
			 * ���B����ʼ��Ϸ
			 */
		case KeyEvent.VK_B:
			if(!gf.b_begin)
			gf.b_begin=true;
			break;
		}
	}
	/**
	 * ���𷽷�
	 * @return	����һö�ӵ�
	 */
	public Shell fire() {
		//������˾Ͳ��Ὺ��
		if(!live) {
			return null;
		}
		//new���ӵ�������ӵ��ӵ�������
		Shell s = new Shell(x+17,y-20,good,dir,gf);
		gf.shells.add(s);
		return s;
	}
	/**
	 * �ɻ���ɻ�֮�����ײ���
	 * @param p ������һ���ɻ�����
	 * @return  �����Ƿ���ײ
	 */
	public boolean collidesWithPlane(Plane p) {
		//����ɻ����Ų��������ɻ����صľ����������������ɻ���good���Բ�ͬ(����ͬһ�ַɻ�)
		if(this.live&&p.isLive()&&this.getRect().intersects(p.getRect())&&this.good!=p.isGood()) {
			//�����ɻ�����
			this.live=false;
			//���ô˷����ķɻ�����ֵ����40
			p.setHp(p.getHp()-40);
			//�������ֵС��0������
			if(p.getHp()<=0)p.setLive(false);
			//����һ����ը�����Ҽ��뵽��ը������
			Explode e = new Explode(x-p.width, y-p.height,gf);
			gf.explodes.add(e);
			//�������ĵз��ɻ����������Ƴ�
			gf.enemyPlanes.remove(this);
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param b ����һ����Ѫ����
	 * ��Ѫ���ߵ���ײ���
	 * ������ҷ��ɻ�������ֵ+50
	 * �Ѽ�Ѫ���ߵ����������Ϊfalse
	 * ���ҴӼ�Ѫ���ߵ�������ȥ��
	 */
	public void collidesWithBlode(BloodProps b) {
		if(this.live&&b.isLife()&&this.getRect().intersects(b.getRect())&&this.good) {
			this.setHp(this.getHp()+50);
			b.setLife(false);
			gf.bloodProps.remove(b);
		}	
	}
	/**
	 * ���ܵ��ߵ���ײ���
	 * @param sp	����һ�����ܵ���
	 * @return		�����Ƿ���ײ
	 * ���ܵ���������Ϊfalse
	 * �ɻ�����(shield)������Ϊtrue
	 * �������ܲ�����ʱ����
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
	 * ȥ�����ܷ���
	 * �����������Ϊtrue����shield(�ɻ��Ƿ�ӵ�л���)����Ϊtrue
	 * ���ܶ���������Ժͷɻ����ܱ�־��Ϊfalse
	 */
	public void removeShield() {
		if(shield==true&&s.live==true) {
		shield=false;
		s.live=false;
		}
	}
	/**
	 * Ѫ���ڲ���
	 * @author �����
	 *
	 */
	public class BloodBar {
		Plane p;
		/**
		 * 
		 * @param p		����һ���ɻ�����
		 */
		BloodBar(Plane p){
			this.p=p;
		}
		public void draw (Graphics g) {
			Color c= g.getColor();
			g.setColor(Color.RED);
			//���ľ�����Ϊ����
			g.drawRect(0, 20, 80, 20);
			int w = 80* p.getHp()/200;
			//ʵ�ľ���ΪѪ��
			g.fillRect(0, 20, w, 20);
			g.setColor(c);
		}
	}
	//��Ѫ����
	public void addHp() {
		this.setHp(1000);
	}
	//�ɻ�����ֵ��get set����
	public int getHp() {
		return hp;
	}
	public void setHp(int life) {
		this.hp = life;
	}
	//�ɻ�������Ե�get is����
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean life) {
		this.live = life;
	}
	//��÷ɻ���good����
	public boolean isGood() {
		return good;
	}
	public boolean isShield() {
		return shield;
	}
}
