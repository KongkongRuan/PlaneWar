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
 * 游戏主窗口类同时也是游戏的主体类
 * 包含了所有线程的启动 、所有游戏物体的重绘和键盘监听
 * @author 闫旭杰
 */
public class MyGameFrame extends Frame {
	File f ;
	static final float WIDE=500;
	static final float HIGH=500;
	//窗口起始坐标
	static final float WINDOW_X= 700;
	static final float WINDOW_Y= 300;
	private int grade=0;
	private int BestGrade;
	public boolean p_stop=false;
	public boolean b_begin=false;
	//加载我方飞机图片
	Image myplaneImg = GameUtil.getImage("images/myplane.png");
	//加载敌方飞机图片
	Image enemyplaneImg = GameUtil.getImage("images/enemyplane.png");
	//加载背景图片
	Image bg0 = GameUtil.getImage("imagesbg/bg0.png");
	Image bg1 = GameUtil.getImage("imagesbg/bg1.png");
	Image bg2 = GameUtil.getImage("imagesbg/bg2.png");
	Image bg3 = GameUtil.getImage("imagesbg/bg3.png");
	Image bg4 = GameUtil.getImage("imagesbg/bg4.png");
	Image bg5 = GameUtil.getImage("imagesbg/bg2.png");
	//在指定坐标创建一个我军战机
	Plane myplane = new Plane(myplaneImg,(int)230,(int)450,Plane.Direction.U,this);
	// TDD 实现随机出现坦克图片(未实现)
	List<DPlane> enemyPlanes=new ArrayList<DPlane>();
	//存放子弹对象的容器
	List<Shell> shells=new ArrayList<Shell>();
	//存放爆炸对象的容器
	List<Explode> explodes=new ArrayList<Explode>();
	//存放加血道具的容器
	List<BloodProps> bloodProps=new ArrayList<BloodProps>();
	//存放护盾道具的容器
	List<ShieldProps> shieldProps=new ArrayList<ShieldProps>();
	//自动被调用,g相当于一个画笔
	public void paint(Graphics g) {
		//画出背景
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
		 * 实现按B键开始游戏
		 * b_begin变量初始值为false
		 * 打开游戏首先显示"按B键开始游戏"
		 */
		if(!b_begin) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.setFont(new Font("宋体",Font.BOLD,40));
			g.drawString("按B键开始游戏", 100, 250);
			g.setColor(c);
		}
		else {
			/*
			 * 实现按P键暂停游戏
			 * b_begin变量初始值为false
			 * 按P键暂停再次敲击P键则开始
			 */
			if(p_stop) {
				Color c = g.getColor();
				g.setColor(Color.WHITE);
				g.setFont(new Font("宋体",Font.BOLD,50));
				g.drawString("按P键继续游戏", 100, 250);
				g.setColor(c);
			}
			else {
				/*最高分数添加到这里更加合理
				 *只要死亡就保存最高分数
				 *并且把最高分数存储到D盘根目录BestGrade.txt文件中
				 *方便下一次开始游戏时读取
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
					/*判断 如果屏幕中的敌方战机小于6架
					 * 通过循环创建五架地方战机并添加到敌方战机容器中
					 */
					if(enemyPlanes.size()<6) {
						for(int i=0;i<5;i++) {
							enemyPlanes.add(new DPlane(enemyplaneImg, 50+400*Math.random(), 0, Direction.D,this));
						}
					}
					myplane.drawSelf(g);//画出我方战机
					/*
					 * 从容器中取出所有炮弹并画出来
					 * 取出后调用hitPlanes方法和所有敌方战机进行碰撞检测
					 * 如果我方战机护盾属性为false则进行碰撞检测
					 * 否则不与我方战机进行碰撞检测(实现了护盾无敌功能)
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
					 * 从容器中取出所有爆炸并画出
					 */
					for(int i=0;i<explodes.size();i++) {
						Explode e= explodes.get(i);
						e.draw(g);
					}
					/*
					 * 从容器中取出所有敌方战机并画出
					 * 同子弹
					 * 如果我方战机护盾属性为false则进行碰撞检测
					 * 否则不与我方战机进行碰撞检测(实现了护盾无敌功能)
					 */
					for(int i=0;i<enemyPlanes.size();i++) {
						DPlane p= enemyPlanes.get(i);
						p.drawSelf(g);
						if(!myplane.isShield()) {
							p.collidesWithPlane(myplane);
						}
					}
					/*
					 * 从容器中取出所有加血道具
					 * 并与我方战机进行碰撞检测
					 */
					for(int i=0;i<bloodProps.size();i++) {
						BloodProps b= bloodProps.get(i);
						b.draw(g);
						myplane.collidesWithBlode(b);
					}
					/*
					 * 从容器中取出所有护盾道具
					 * 并与我方战机进行碰撞检测
					 */
					for(int i=0;i<shieldProps.size();i++) {
						ShieldProps s= shieldProps.get(i);
						s.draw(g);
						myplane.collidesWithShieldProps(s);
					}
					/*
					 * 1.子弹容器中子弹数量
					 * 2.爆炸容器中爆炸对象数量
					 * 3.敌方战机容器中敌方战机数量
					 * 4.按键功能说明
					 * 5.当前分数
					 * 6.最高分数
					 */
					Color c = g.getColor();
					g.setColor(Color.WHITE);
					g.drawString("Shells count:"+shells.size(), 20, 470);
					g.drawString("Explodes count:"+explodes.size(), 20, 450);
					g.drawString("EnemyPlanes count:"+enemyPlanes.size(), 20, 430);
					g.drawString("MyPlane life:"+myplane.getHp(), 20, 410);
					g.drawString("X发射子弹  P暂停 F2复活", 20, 390);
					g.drawString("Grade:"+this.getGrade(), 400, 50);
					g.drawString("BestGrade:"+this.getBestGrade(), 400, 70);
					g.setColor(c);
			}
		}
	}
	/*
	 * 主窗口线程
	 * 采用了直接继承Thread类的形式
	 * 然后重写run方法
	 * 每执行一次repaint方法线程休眠40毫秒
	 * 等价于40毫秒重画一次 刷新频率为40毫秒
	 */
	class PaintThread extends Thread{
			public void run() {
				while(true) {
					//运行重画方法
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
	 * 添加键盘监听
	 * 重写keyPressed和keyReleased方法
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
	 * 设置游戏窗口属性
	 */
	public void launchFrame() {
		/*
		 * 从D盘根目录BestGrade.txt文件中读取最高分数
		 * 如果文件不存在则新建文件
		 */
		//定位文件
		f = new File("D:\\","BestGrade.txt");
		//如果文件不存在
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
					String s = bfr.readLine();
					if(s!=null&&s!=""){
						setBestGrade(Integer.parseInt(s));
					}else {
						setBestGrade(0);
					}
					bfr.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		//初始化敌方飞机
		for(int i=0;i<10;i++) {
			enemyPlanes.add(new DPlane(enemyplaneImg, 50+400*Math.random(), 0, Direction.D,this));
		}
		
		//窗口标题
		this.setTitle("飞机大战");
		//设置窗口大小
		this.setSize((int)HIGH,(int)WIDE );
		//窗口位置(窗口左上角坐标)
		this.setLocation((int)WINDOW_X,(int)WINDOW_Y);
		//启动重画线程
		new PaintThread().start();
		//增加键盘监听
		this.addKeyListener(new KeyMonitor());
		//使用户不能更改窗口大小
		this.setResizable(false);
		//窗口可见
		this.setVisible(true);
		/*
		 * 使用匿名内部类添加右上角红叉关闭游戏的功能
		 */
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	/*
	 * 主方法
	 */
	public static void main(String[]args) {
		MyGameFrame mf = new MyGameFrame();
		mf.launchFrame();
	}
	/**
	 * 双缓冲区解决屏幕闪烁问题
	 */
	private Image offScreenImage = null;
    public void update(Graphics g) {
        if(offScreenImage == null)
            offScreenImage = this.createImage((int)WIDE,(int)HIGH);
         
        Graphics gOff = offScreenImage.getGraphics();
  
        paint(gOff);
        g.drawImage(offScreenImage, 0, 0, null);
    }
    //分数的get set方法
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	//最高分的get set方法
	public int getBestGrade() {
		return BestGrade;
	}
	public void setBestGrade(int bestGrade) {
		BestGrade = bestGrade;
	}
}
