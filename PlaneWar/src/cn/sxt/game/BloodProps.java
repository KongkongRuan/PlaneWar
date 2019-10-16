package cn.sxt.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
/**
 * 加血道具类
 * 碰撞边界反弹的算法
 * @author 闫旭杰
 *
 */
public class BloodProps extends GameObject{
	//首先加载加血道具的图片
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
		
		//血球道具按照任意角飞行
		x+=speed*Math.cos(degree);
		y+=speed*Math.sin(degree);
		//碰撞边界反弹
		if(x<width||x>(int)MyGameFrame.WIDE-width) {
			degree =Math.PI-degree;
		}
		if(y<35||y>(int)MyGameFrame.HIGH-(height+5)) {
			degree =-degree;
		}
		
	}
	//加血道具生死的is set方法
	public boolean isLife() {
		return life;
	}

	public void setLife(boolean life) {
		this.life = life;
	}
}