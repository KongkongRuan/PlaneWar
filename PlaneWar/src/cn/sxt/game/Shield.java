package cn.sxt.game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
/**
 * 护盾类
 * 产生护盾后飞机无敌
 * @author 闫旭杰
 *
 */
public class Shield extends GameObject{
	//初始化护盾图片
	Image shieldImg = GameUtil.getImage("images/shield.png");
	boolean live=true;
	Plane p;
	MyGameFrame gf;
	public Shield(Plane p) {
		this.p=p;
	}
	public void draw(Graphics g) {
		//死了不画  敌方飞机不画
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