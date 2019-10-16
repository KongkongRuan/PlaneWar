package cn.sxt.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
/**
 * 护盾道具类
 * 碰撞边界反弹的算法
 * @author 闫旭杰
 *
 */
public class ShieldProps extends GameObject{
	Image shieldpropsImg = GameUtil.getImage("images/shieldprops.png");
	double degree;
	boolean life=true;
	public ShieldProps() {
		x=200;
		y=200;
		width=20;
		height=20;
		speed =3;
		degree = Math.random()*Math.PI*2;
	}
	public void draw(Graphics g) {
		if(!life)return;
		g.drawImage(shieldpropsImg,(int)x,(int)y,null);
		//护盾道具按照任意角飞行
		x+=speed*Math.cos(degree);
		y+=speed*Math.sin(degree);	
		//碰撞边界反弹的算法
		if(x<width||x>(int)MyGameFrame.WIDE-width) {
			degree =Math.PI-degree;
		}
		if(y<35||y>(int)MyGameFrame.HIGH-(height+5)) {
			degree =-degree;
		}
	}
	//护盾道具生死的is set方法
	public boolean isLife() {
		return life;
	}
	public void setLife(boolean life) {
		this.life = life;
	}
}