package cn.sxt.game;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import cn.sxt.game.Plane.Direction;
/**
 * ��Ϸ��������ͬʱҲ����Ϸ��������
 * �����������̵߳����� ��������Ϸ������ػ�ͼ��̼���
 * @author �����
 */
public class MyGameFrame extends Frame {
	File f ;
	static final float WIDE=500;
	static final float HIGH=500;
	//������ʼ����
	static final float WINDOW_X= 700;
	static final float WINDOW_Y= 300;
	private int grade=0;
	private int BestGrade;
	public boolean p_stop=false;
	public boolean b_begin=false;
	//�����ҷ��ɻ�ͼƬ
	Image myplaneImg = GameUtil.getImage("images/myplane.png");
	//���صз��ɻ�ͼƬ
	Image enemyplaneImg = GameUtil.getImage("images/enemyplane.png");
	//���ر���ͼƬ
	Image bg0 = GameUtil.getImage("imagesbg/bg0.png");
	Image bg1 = GameUtil.getImage("imagesbg/bg1.png");
	Image bg2 = GameUtil.getImage("imagesbg/bg2.png");
	Image bg3 = GameUtil.getImage("imagesbg/bg3.png");
	Image bg4 = GameUtil.getImage("imagesbg/bg4.png");
	Image bg5 = GameUtil.getImage("imagesbg/bg2.png");
	//��ָ�����괴��һ���Ҿ�ս��
	Plane myplane = new Plane(myplaneImg,(int)230,(int)450,Plane.Direction.U,this);
	// TDD ʵ���������̹��ͼƬ(δʵ��)
	List<DPlane> enemyPlanes=new ArrayList<DPlane>();
	//����ӵ����������
	List<Shell> shells=new ArrayList<Shell>();
	//��ű�ը���������
	List<Explode> explodes=new ArrayList<Explode>();
	//��ż�Ѫ���ߵ�����
	List<BloodProps> bloodProps=new ArrayList<BloodProps>();
	//��Ż��ܵ��ߵ�����
	List<ShieldProps> shieldProps=new ArrayList<ShieldProps>();
	//�Զ�������,g�൱��һ������
	public void paint(Graphics g) {
		//��������
		g.drawImage(bg0,0,0,null);
		if(getGrade()>500&&getGrade()<=1000)
		g.drawImage(bg1,0,0,null);
		if(getGrade()>1000&&getGrade()<=1500)
		g.drawImage(bg2,0,0,null);
		if(getGrade()>1500&&getGrade()<=2000)
		g.drawImage(bg3,0,0,null);
		if(getGrade()>2000&&getGrade()<=2500)
		g.drawImage(bg4,0,0,null);
		if(getGrade()>2500)
		g.drawImage(bg5,0,0,null);
		/*
		 * ʵ�ְ�B����ʼ��Ϸ
		 * b_begin������ʼֵΪfalse
		 * ����Ϸ������ʾ"��B����ʼ��Ϸ"
		 */
		if(!b_begin) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.setFont(new Font("����",Font.BOLD,40));
			g.drawString("��B����ʼ��Ϸ", 100, 250);
			g.setColor(c);
		}
		else {
			/*
			 * ʵ�ְ�P����ͣ��Ϸ
			 * b_begin������ʼֵΪfalse
			 * ��P����ͣ�ٴ��û�P����ʼ
			 */
			if(p_stop) {
				Color c = g.getColor();
				g.setColor(Color.WHITE);
				g.setFont(new Font("����",Font.BOLD,50));
				g.drawString("��P��������Ϸ", 100, 250);
				g.setColor(c);
			}
			else {
				/*��߷�����ӵ�������Ӻ���
				 *ֻҪ�����ͱ�����߷���
				 *���Ұ���߷����洢��D�̸�Ŀ¼BestGrade.txt�ļ���
				 *������һ�ο�ʼ��Ϸʱ��ȡ
				 */
				if(!myplane.isLive()) {
					if(getBestGrade()<getGrade()) {
						setBestGrade(getGrade());
						}
						setGrade(0);
						try {
							BufferedWriter bfw = new BufferedWriter(new FileWriter(f));
							bfw.write(getBestGrade()+"");
							bfw.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
				}
					/*�ж� �����Ļ�еĵз�ս��С��6��
					 * ͨ��ѭ��������ܵط�ս������ӵ��з�ս��������
					 */
					if(enemyPlanes.size()<6) {
						for(int i=0;i<5;i++) {
							enemyPlanes.add(new DPlane(enemyplaneImg, 50+400*Math.random(), 0, Direction.D,this));
						}
					}
					myplane.drawSelf(g);//�����ҷ�ս��
					/*
					 * ��������ȡ�������ڵ���������
					 * ȡ�������hitPlanes���������ез�ս��������ײ���
					 * ����ҷ�ս����������Ϊfalse�������ײ���
					 * �������ҷ�ս��������ײ���(ʵ���˻����޵й���)
					 */
					for (int i = 0; i < shells.size(); i++) {
						Shell m = shells.get(i);
						m.hitPlanes(enemyPlanes);
						if(!myplane.isShield()) {
						m.hitPlane(myplane);
						}
							m.drawSelf(g);
					}
					/*
					 * ��������ȡ�����б�ը������
					 */
					for(int i=0;i<explodes.size();i++) {
						Explode e= explodes.get(i);
						e.draw(g);
					}
					/*
					 * ��������ȡ�����ез�ս��������
					 * ͬ�ӵ�
					 * ����ҷ�ս����������Ϊfalse�������ײ���
					 * �������ҷ�ս��������ײ���(ʵ���˻����޵й���)
					 */
					for(int i=0;i<enemyPlanes.size();i++) {
						DPlane p= enemyPlanes.get(i);
						p.drawSelf(g);
						if(!myplane.isShield()) {
							p.collidesWithPlane(myplane);
						}
					}
					/*
					 * ��������ȡ�����м�Ѫ����
					 * �����ҷ�ս��������ײ���
					 */
					for(int i=0;i<bloodProps.size();i++) {
						BloodProps b= bloodProps.get(i);
						b.draw(g);
						myplane.collidesWithBlode(b);
					}
					/*
					 * ��������ȡ�����л��ܵ���
					 * �����ҷ�ս��������ײ���
					 */
					for(int i=0;i<shieldProps.size();i++) {
						ShieldProps s= shieldProps.get(i);
						s.draw(g);
						myplane.collidesWithShieldProps(s);
					}
					/*
					 * 1.�ӵ��������ӵ�����
					 * 2.��ը�����б�ը��������
					 * 3.�з�ս�������ез�ս������
					 * 4.��������˵��
					 * 5.��ǰ����
					 * 6.��߷���
					 */
					Color c = g.getColor();
					g.setColor(Color.WHITE);
					g.drawString("Shells count:"+shells.size(), 20, 470);
					g.drawString("Explodes count:"+explodes.size(), 20, 450);
					g.drawString("EnemyPlanes count:"+enemyPlanes.size(), 20, 430);
					g.drawString("MyPlane life:"+myplane.getHp(), 20, 410);
					g.drawString("X�����ӵ�  P��ͣ F2����", 20, 390);
					g.drawString("Grade:"+this.getGrade(), 400, 50);
					g.drawString("BestGrade:"+this.getBestGrade(), 400, 70);
					g.setColor(c);
			}
		}
	}
	/*
	 * �������߳�
	 * ������ֱ�Ӽ̳�Thread�����ʽ
	 * Ȼ����дrun����
	 * ÿִ��һ��repaint�����߳�����40����
	 * �ȼ���40�����ػ�һ�� ˢ��Ƶ��Ϊ40����
	 */
	class PaintThread extends Thread{
			public void run() {
				while(true) {
					//�����ػ�����
					repaint();
					try {
						Thread.sleep(40);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
		}
	}
	/*
	 * ��Ӽ��̼���
	 * ��дkeyPressed��keyReleased����
	 */
	
	class KeyMonitor extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			myplane.addDirection(e);
		}

		public void keyReleased(KeyEvent e) {
			myplane.minusDirection(e);
		}
	}
	
	/**
	 * ������Ϸ��������
	 */
	public void launchFrame() {
		/*
		 * ��D�̸�Ŀ¼BestGrade.txt�ļ��ж�ȡ��߷���
		 * ����ļ����������½��ļ�
		 */
		//��λ�ļ�
		f = new File("D:\\","BestGrade.txt");
		//����ļ�������
		if(!f.exists()){
			try {
				f.createNewFile();
				}catch(IOException e) {
					e.printStackTrace();
				}
			setBestGrade(0);
			}else {
				try {
					BufferedReader bfr = new BufferedReader(new FileReader(f));
					setBestGrade(Integer.parseInt(bfr.readLine()));
					bfr.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		//��ʼ���з��ɻ�
		for(int i=0;i<10;i++) {
			enemyPlanes.add(new DPlane(enemyplaneImg, 50+400*Math.random(), 0, Direction.D,this));
		}
		
		//���ڱ���
		this.setTitle("�ɻ���ս");
		//���ô��ڴ�С
		this.setSize((int)HIGH,(int)WIDE );
		//����λ��(�������Ͻ�����)
		this.setLocation((int)WINDOW_X,(int)WINDOW_Y);
		//�����ػ��߳�
		new PaintThread().start();
		//���Ӽ��̼���
		this.addKeyListener(new KeyMonitor());
		//ʹ�û����ܸ��Ĵ��ڴ�С
		this.setResizable(false);
		//���ڿɼ�
		this.setVisible(true);
		/*
		 * ʹ�������ڲ���������ϽǺ��ر���Ϸ�Ĺ���
		 */
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	/*
	 * ������
	 */
	public static void main(String[]args) {
		MyGameFrame mf = new MyGameFrame();
		mf.launchFrame();
	}
	/**
	 * ˫�����������Ļ��˸����
	 */
	private Image offScreenImage = null;
    public void update(Graphics g) {
        if(offScreenImage == null)
            offScreenImage = this.createImage((int)WIDE,(int)HIGH);
         
        Graphics gOff = offScreenImage.getGraphics();
  
        paint(gOff);
        g.drawImage(offScreenImage, 0, 0, null);
    }
    //������get set����
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	//��߷ֵ�get set����
	public int getBestGrade() {
		return BestGrade;
	}
	public void setBestGrade(int bestGrade) {
		BestGrade = bestGrade;
	}
}
