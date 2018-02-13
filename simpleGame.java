//Game3.java

//HOT DIGGITY DOG!

import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;

public class simpleGame extends JFrame implements ActionListener{ //inherits from JFrame, JFrame and More :O
	//ACtionLIstner is a interface. BArgain- we provide the methods it wants, and it will do stuff when needed
	Timer myTimer;
	Timer powerUpTimer;
	GamePanel game;


	public simpleGame(){
		super("Tron"); //calls constructor of super frame, must be first line of constructor
		setSize(850,800);
		myTimer = new Timer(35,this);
		myTimer.start();
		game = new GamePanel();
		add(game);

		setResizable(false);
		setVisible(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//powerUpTimer = new Timer(5000,this);
		//powerUpTimer.start();
		//System.out.println(powerUpTimer);
	}

	public void actionPerformed(ActionEvent e){
		if(game != null){
			game.twoPlayer();
			//game.onePlayer();

			//if sp >> only 1 P
			//game.pOneMove();

			//if tp >> have both
			//game.pOneMove();
			//game.pTwoMove();

			game.repaint();
		}
	}

	public static void main(String[]args){
		new simpleGame();
	}
}

class GamePanel extends JPanel implements KeyListener{ //Keyboard is an interface
	//private int boxx1, boxy1, boxx2, boxy2, dx, dy;
	private boolean [] keys;
	private Spot box1;
	private Spot box2;
	private int turbo1, turbo2;
	private int lifeTime = 100;
	private int newObject = 200;
	private boolean objectOnScreen, blitted;
	private int px = 0, py = 0;
	//private ArrayList<PowerUp> powerUps = ["warpWall", "extraTurbo", "doubleSpeed"];
	//private int r = 0, v = 0, b = 0;
	//private Rectangle p;
	//private Timer powerUpTimer;

	private int p1_Points, p2_Points, p1wins, p2wins;

	Image p1, p2;
	//private ArrayList<Trail>path = new ArrayList<Trail>();

	public GamePanel(){
		nextRound();

		p1_Points = 0;
		p2_Points = 0;

		turbo1 = 3;
		turbo2 = 3;

		setFocusable(true);
		addKeyListener(this);
		requestFocus();

		p1 = new ImageIcon("oRect.png").getImage();
		p2 = new ImageIcon("gRect.png").getImage();

		keys = new boolean[KeyEvent.KEY_LAST+1];

		objectOnScreen = false;
		blitted = false;
	}

	@Override //think I am overriding one of the methods in JPanel
	public void paintComponent(Graphics g){ //supply same parameters that replaces it
		g.setColor(new Color(0,0,0));
		g.fillRect(0,0,getWidth(),getHeight());

		g.setColor(new Color(125,125,125));
		g.fillRect(50,210,750,520);

		g.setColor(new Color(200,200,200));
		g.fillRect(50,40,125,125);
		g.fillRect(675,40,125,125);

		g.setColor(new Color(255,255,255));
		g.fillRect(325,700,200,25);

		g.drawImage(p1,box1.getX(),box1.getY(),this);
		g.drawImage(p2,box2.getX(),box2.getY(),this);

		//int px = 0, py = 0;
		int r = 0, v = 0, b = 0;

		if(objectOnScreen == true){
			System.out.println(blitted);
			if(blitted == true){
				Random rand = new Random();

				px = rand.nextInt(750);
				py = rand.nextInt(520);

				r = rand.nextInt(255);
				v = rand.nextInt(255);
				b = rand.nextInt(255);

				//p = new Rectangle(px+50,py+210,10,10);

				blitted = false;
			}

			g.setColor(new Color(r,v,b));
			g.fillRect(px+50,py+210,30,30);
		}
	}

	//ALL THREE MUST BE PRESENT
	public void keyPressed(KeyEvent e){
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e){
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e){} //don't use this for games

	public void onePlayer(){
		pMove();
		cMove();

		box1.move();
		box2.move();
	}

	public void twoPlayer(){
		int p1wins = 0;
		int p2wins = 0;

		pMove();
		//pOneMove();
		//pTwoMove();

		box1.move();
		box2.move();

		//if(box.)
		//tick();

		//if(box1.intersection())
		//paintTrail();
	}

	public void tick(){
		
		if(objectOnScreen == false){
			lifeTime = 50;
			newObject -= 1;

			if(newObject == 0){
				objectOnScreen = true;
				blitted = true;
			}
		}

		else if(objectOnScreen == true){
			newObject = 100;
			lifeTime -= 1;

			if(lifeTime == 0){
				objectOnScreen = false;
				blitted = false;
			}
		}
	}

	public void pMove(){
		if(box1.getHit() == false && box2.getHit() == false){
			tick();
			if(box1.getX() == px+50 && box1.getY() == py+210){
				box1.warpWall();
				lifeTime = 0;
			}

			//enter for turbo >> P1
			if(keys[KeyEvent.VK_ENTER]){
				box1.turbo();
			}
			//change direction of p1
			if(keys[KeyEvent.VK_RIGHT]){
				if(box1.moveD() != "left"){
					box1.moveRight();
				}
			}
			else if(keys[KeyEvent.VK_LEFT]){
				if(box1.moveD() != "right"){
					box1.moveLeft();
				}
			}

			else if(keys[KeyEvent.VK_UP]){
				if(box1.moveD() != "down"){
					box1.moveUp();
				}
			}
			else if(keys[KeyEvent.VK_DOWN]){
				if(box1.moveD() != "up"){
					box1.moveDown();				
				}
			}

			//tab for turbo >> p2
			if(box2.getX() == px+50 && box2.getY() == py+210){
				box2.warpWall();
				lifeTime = 0;
			}
			if(keys[KeyEvent.VK_TAB]){
				box2.turbo();
			}
			//change direction of p2
			if(keys[KeyEvent.VK_D]){
				if(box2.moveD() != "left"){
					box2.moveRight();
				}
			}
			else if(keys[KeyEvent.VK_A]){
				if(box2.moveD() != "right"){
					box2.moveLeft();
				}
			}
			else if(keys[KeyEvent.VK_W]){
				if(box2.moveD() != "down"){
					box2.moveUp();
				}
			}
			else if(keys[KeyEvent.VK_S]){
				if(box2.moveD() != "up"){
					box2.moveDown();
				}
			}
		}

		else if(box1.getHit() == true && box2.getHit() == true){
			//System.out.println("DRAW");

			//box1.newRound();
			//box2.newRound();

			//System.out.println(box1.getHit());

			if(keys[KeyEvent.VK_SPACE]){
				nextRound();
			}
		}

		else{
			box1.noMove();
			box2.noMove();

			if(box1.getHit() == true){
				p2wins += 1;
				//box1.nextRound();
				System.out.println(box1.getHit());
			}

			else if(box2.getHit() == true){
				p1wins += 1;
				//box2.nextRound();

			}

			if(keys[KeyEvent.VK_SPACE]){
				nextRound();
			}
		}
	}

	public void cMove(){ //computer moves
		if(box2.getHit() == false && box1.getHit() == false){
			Random rand = new Random();

			int r = rand.nextInt(4);

			if(r == 0){
				if(box2.moveD() != "left"){
					box2.moveRight();
				}
			}
			else if(r == 1){
				if(box2.moveD() != "right"){
					box2.moveLeft();
				}
			}
			else if(r == 2){
				if(box2.moveD() != "down"){
					box2.moveUp();
				}
			}
			else if(r == 3){
				if(box2.moveD() != "up"){
					box2.moveDown();
				}
			}
		}

		else{
			box2.noMove();
		}
	}

	public void nextRound(){
		System.out.println("New Round");
		box1 = new Spot(600,470);
		box2 = new Spot(250,470);

		System.out.println("P1 Wins: "+p1wins);
		System.out.println("P2 Wins: "+p2wins);

		box1.moveLeft();
		box2.moveRight();

		if(p1wins == 3){
			endBattle(box1,box2);
		}

		else if(p2wins == 3){
			endBattle(box2,box1);
		}
	}

	public void endBattle(Spot winner, Spot loser){
		System.out.println("END OF BATTLE");
		
	}
}







