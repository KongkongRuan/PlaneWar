package cn.sxt.game;
import java.awt.Image;
/**
 * �з��ɻ���(��û����ȫ�������� �̳���Plane��)
 * 1.ӵ���Լ����еĹ��췽��
 * 2.�Լ����еĹ��췽��
 * 3.�Լ����еĿ��𷽷�
 * @author �����
 */
public class DPlane extends Plane{
	static final float DPLANE_SPEED=3;
	public DPlane(Image img, double x, double y, Direction dir,MyGameFrame gf) {
		super(img, x, y, dir,gf);
	}
	/*ÿ���ƶ�����1-3֮�����
	 * �������ֱ������
	 * 1/59�ļ��ʷ����ӵ�
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
	 * �л����𷽷�
	 * 1.���˲��Ὺ��
	 * 2.�ӵ�����̶�����
	 * 3.�����ɵ��ӵ�Ҳ��ӵ��ӵ�������
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
