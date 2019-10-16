package cn.sxt.game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
/**
 * ������
 * �������ܺ�ɻ��޵�
 * @author �����
 *
 */
public class Shield extends GameObject{
	//��ʼ������ͼƬ
	Image shieldImg = GameUtil.getImage("images/shield.png");
	boolean live=true;
	Plane p;
	MyGameFrame gf;
	public Shield(Plane p) {
		this.p=p;
	}
	public void draw(Graphics g) {
		//���˲���  �з��ɻ�����
		if(!live)return;
		if(!p.isGood())return;
		g.drawImage(shieldImg,(int)p.x,(int)p.y,null);
	}
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
}