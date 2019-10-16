package cn.sxt.game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
/**
 * �ӵ���
 * 1.�ӵ��Ļ�������
 * 2.ʵ���ӵ����ƶ�
 * 3.�����ɻ��Ĺ���(����������������)
 * @author �����
 */
public class Shell extends GameObject{
	//�����ӵ�ͼƬ
	Image shellImg = GameUtil.getImage("images/shell.png");
	static final float SHELL_SPEED=6;
	//�ӷɻ����л�õķ���
	Plane.Direction dir;
	private boolean live = true;
	private boolean good; 
	private MyGameFrame gf;
	/**
	 * 
	 * @param x		��ʼX����
	 * @param y		��ʼY����
	 * @param good	�ӵ��ĺû�����
	 * @param dir	�ӵ�����
	 * @param gf	����һ������MyGameFrame������
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
	//�����ӵ�
	public void drawSelf(Graphics g) {
		if(!live)return;
		g.drawImage(shellImg,(int)x,(int)y,null);
		move();
	}
	/**
	 * �ӵ����ƶ�����
	 * �����ƶ�
	 * ÿ���ƶ�speed��С�ľ���
	 * �������ֱ���������Ҵ��ӵ��������Ƴ�
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
	 * �����ɻ��ķ���
	 * @param plane	����һ���ɻ�����
	 * @return	�����Ƿ���ײ
	 */
	public boolean hitPlane(Plane plane) {
		//����ӵ����Ų����ӵ��ͷɻ���ײ���ҷɻ����Ų����ӵ��ͷɻ�good�����෴
		if(this.live&&this.getRect().intersects(plane.getRect())&&plane.live&&this.good!=plane.isGood()) {
			if(plane.isGood()) {
				plane.setHp(plane.getHp()-20);
				if(plane.getHp()<=0) {
					plane.setLive(false);
				}
			}else {
				/*
				 * ����ǵз��ɻ���ֱ������
				 * ��������10
				 * 50�ֵ�ʱ�����һ����Ѫ����
				 * 100�ֵ�ʱ�����һ�����ܵ���
				 * �������������200����һ����Ѫ����
				 * �������������350����һ�����ܵ���
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
			 * ��ײ���ӵ��������Ҵ��ӵ�������ȥ��
			 * ����ײλ�ò���һ����ը���󲢷��뱬ը������
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
	 * �ӵ��͵л�������ײ
	 * �͵����ɻ���ײ���һ��forѭ������
	 * @param enemyPlanes
	 * @return	�����Ƿ���ײ
	 */
	public boolean hitPlanes(List<DPlane> enemyPlanes) {
		for (int i = 0; i < enemyPlanes.size(); i++) {
			if(hitPlane(enemyPlanes.get(i))) {
				return true;
			}
		}
		return false;
	}
	//�ӵ��Ƿ����set is����
	public void setLive(boolean live) {
		this.live = live;
	}
	public boolean isLive() {
		return live;
	}
}
