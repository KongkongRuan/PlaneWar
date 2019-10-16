package cn.sxt.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
/**
 * ��Ѫ������
 * ��ײ�߽練�����㷨
 * @author �����
 *
 */
public class BloodProps extends GameObject{
	//���ȼ��ؼ�Ѫ���ߵ�ͼƬ
	Image bloodImg = GameUtil.getImage("images/blood.png");
	double degree;
	boolean life=true;
	public BloodProps() {
		x=200;
		y=200;
		width=bloodImg.getWidth(null);
		height=bloodImg.getHeight(null);
		speed =3;
		degree = Math.random()*Math.PI*2;
	}
	public void draw(Graphics g) {
		if(!life)return;
		g.drawImage(bloodImg,(int)x,(int)y,null);
		
		//Ѫ����߰�������Ƿ���
		x+=speed*Math.cos(degree);
		y+=speed*Math.sin(degree);
		//��ײ�߽練��
		if(x<width||x>(int)MyGameFrame.WIDE-width) {
			degree =Math.PI-degree;
		}
		if(y<35||y>(int)MyGameFrame.HIGH-(height+5)) {
			degree =-degree;
		}
		
	}
	//��Ѫ����������is set����
	public boolean isLife() {
		return life;
	}

	public void setLife(boolean life) {
		this.life = life;
	}
}