//Game3.java

//HOT DIGGITY DOG!

import java.util.Random;
import java.util.ArrayList;
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
		setSize(855,800);
		myTimer = new Timer(40,this);
		myTimer.start();
		game = new GamePanel();
		add(game);

		setResizable(false);
		setVisible(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	private boolean [] keys;
	private Spot box1;
	private Spot box2;

	private int p1_Points, p2_Points, p1wins, p2wins;
	private boolean pointAdded;

	private Rectangle p;
	private int px = 0, py = 0;
	private int onScreenLifeTime = 500;
	private int blitNewObject = 10;
	private int timeLeft = 140;
	private int turboTime1 = 5, turboTime2 = 5;
	private boolean objectOnScreen, blitted, roundOver;
	private int powerTaken; //0 for not taken, 1 for player 1, 2 for player 2
	private int randPowerUp;

	/*RAND POWERS
	0 - extra turbo
	1 - portal
	2 - double speed
	3 - shield
	4 - reverse opp. controls
	5 - clear trails
	6 - turbo

	POWERTAKEN
	0 - None
	1 - Box 1
	2 - Box 2
	*/

	//==============Constructor=============
	public GamePanel(){
		nextRound();

		p = new Rectangle(0,0,10,10);

		p1_Points = 0;
		p2_Points = 0;

		p1wins = 2;
		p2wins = 3;

		setFocusable(true);
		addKeyListener(this);
		requestFocus();

		keys = new boolean[KeyEvent.KEY_LAST+1];
	}

	//==============Drawing Stuff==============
	@Override //think I am overriding one of the methods in JPanel
	public void paintComponent(Graphics g){ //supply same parameters that replaces it
		//Background	
		g.setColor(new Color(0,0,0));
		g.fillRect(0,0,getWidth(),getHeight());

		//Board
		g.setColor(new Color(125,125,125));
		g.fillRect(60,110,730,635);

		//Player Cards
		g.setColor(new Color(200,200,200));
		g.fillRect(20,20,75,75);
		g.fillRect(760,20,75,75);

		//Bottom Bar
		g.fillRect(60,710,730,35);

		//Turbo Bar
		g.fillRect(135,65,80,30);
		g.fillRect(650,65,80,30);

		//Power Up Timer
		g.fillRect(105,20,10,75);
		g.fillRect(740,20,10,75);

		//=====Rounds Won Bar====
		//Number Square
		g.setColor(new Color(17,122,72));
		g.fillRect(240,40,40,40);
		g.fillRect(575,40,40,40);

		//Fill Bar
		g.fillRect(295,55,119,5);
		g.fillRect(439,55,119,5);

		//Space Bar
		g.setColor(new Color(255,255,255));
		g.fillRect(200,712,450,31);

		//========Trails========
		ArrayList<Rectangle>aTrail = box1.returnTrail();
		g.setColor(Color.red);

		//Player 1 Trail
		for(int i = 0; i < aTrail.size(); i++){
			Rectangle trail = aTrail.get(i);
			g.fillRect((int)trail.getX(),(int)trail.getY(),(int)trail.getWidth(),(int)trail.getHeight());
		}

		//turbo left
		for(int i = 0; i < box1.countTurboLeft(); i++){
			g.fillRect(140+25*i,70,20,20);
		}

		//p1 win bar
		for(int i = 0; i < p1wins; i++){
			g.fillRect(297+39*i,56,37,3);
		}

		//time left
		g.setColor(new Color(100,0,0));
		g.fillRect(107,22,6,70);

		//time gone
		g.setColor(Color.red);
		if(powerTaken == 1){
			g.fillRect(107,22+(140-timeLeft)/2,6,70-(140-timeLeft)/2);
			//System.out.println(timeLeft);
		}

		//turbo counters
		g.setColor(new Color(100,0,0));
		for(int i = 3 - box1.countTurboLeft(); i > 0; i--){
			g.fillRect(215-25*i,70,20,20);
		}

		//empty win counter
		for(int i = 3-p1wins; i > 0; i--){
			g.fillRect(414-39*i,56,37,3);
		}

		//Player 2 Trail
		ArrayList<Rectangle>bTrail = box2.returnTrail();
		g.setColor(Color.blue);

		for(int i = 0; i < bTrail.size(); i++){
			Rectangle trail2 = bTrail.get(i);
			g.fillRect((int)trail2.getX(),(int)trail2.getY(),(int)trail2.getWidth(),(int)trail2.getHeight());
		}

		//turbo left
		for(int i = 0; i < box2.countTurboLeft(); i++){
			g.fillRect(655+25*i,70,20,20);
		}

		//p2 wins bar
		for(int i = 0; i < p2wins; i++){
			g.fillRect(441+39*i,56,37,3);
		}

		//time gone
		g.setColor(new Color(0,0,100));
		g.fillRect(742,22,6,70);

		//time left
		g.setColor(Color.blue);
		if(powerTaken == 2){
			g.fillRect(742,22+(140-timeLeft)/2,6,70-(140-timeLeft)/2);
			System.out.println(timeLeft);
		}

		//empty turbo counter
		g.setColor(new Color(0,0,100));
		for(int i = 3 - box2.countTurboLeft(); i > 0; i--){
			g.fillRect(730-25*i,70,20,20);
		}

		//empty win bar
		for(int i = 3-p2wins; i > 0; i--){
			g.fillRect(558-39*i,56,37,3);
		}

		//Players (Ovals)
		g.setColor(new Color(200,200,200));
		g.fillOval(box1.returnX()-1,box1.returnY()-1,6,6);
		g.fillOval(box2.returnX()-1,box2.returnY()-1,6,6);

		g.setColor(Color.red);
		g.fillOval(box1.returnX(),box1.returnY(),4,4);

		g.setColor(Color.blue);
		g.fillOval(box2.returnX(),box2.returnY(),4,4);

		//Colliding Rectangles (Testing Only - Delete afterwards)
		Rectangle rect1 = box1.drawR();
		Rectangle rect2 = box2.drawR();

		g.setColor(new Color(255,255,0));
		//g.drawRect((int)rect1.getX(),(int)rect1.getY(),(int)rect1.getWidth(),(int)rect1.getHeight());

		g.setColor(new Color(0,255,255));
		//g.drawRect((int)rect2.getX(),(int)rect2.getY(),(int)rect2.getWidth(),(int)rect2.getHeight());

		//Power Ups
		if(objectOnScreen == true && powerTaken == 0){ //if on screen
			if(blitted == false){ //choose random powerup
				randPowerUp = randomPowerUp();

				Random rand = new Random();

				px = rand.nextInt(720);
				py = rand.nextInt(590);

				p.setLocation(px+60,py+110);

				blitted = true;
			}

			if(randPowerUp == 0){
				//System.out.println("Extra Turbo");
				g.setColor(new Color(255,255,0));
			}

			if(randPowerUp == 1){
				//System.out.println("Turbo");
				g.setColor(new Color(0,255,255));
			}

			if(randPowerUp == 2){
				//System.out.println("Double Speed");
				g.setColor(new Color(255,0,255));
			}

			if(randPowerUp == 3){
				//System.out.println("Shield");
				g.setColor(new Color(255,255,255));
			}

			if(randPowerUp == 4){
				g.setColor(new Color(255,140,0));
			}

			if(randPowerUp == 5){
				g.setColor(new Color(0,255,0));
			}

			g.fillRect(px+60,py+110,10,10);

			g.setColor(new Color(189,255,0));
			g.drawRect((int)p.getX(),(int)p.getY(),(int)p.getWidth(),(int)p.getHeight());
		}
	}

	//===================Moving Players===================
	/*public void onePlayer(){
		pMove();
		cMove();

		box1.move();
		box2.move();
	}*/

	public void twoPlayer(){
		pMove();

		box1.move();
		box2.move();


		//System.out.println(box1.returnX());

		box2.noMove();

		/*if(box1.turboInitiated()){
			box1.turbo();
			box1.noTurbo();

			System.out.println("Turbo Time");

			turboTime = 3;
			powerTaken = 3;
		}*/

		if(box1.getPowerUp(p)){
			//System.out.println("I got it!");
			powerTaken = 1;
			objectOnScreen = false;
			timeLeft = 140;
		}

		if(box2.getPowerUp(p)){
			//System.out.println("I got it!");
			powerTaken = 2;
			objectOnScreen = false;
			timeLeft = 140;
		}

		/*else if(box2.getPowerUp(p)){
			timeLeft = 700;
			onScreenLifeTime = 0;
			powerTaken = 2;
			powerCountDown(box2);
		}*/

		//returnRect - give players collision rectangle

		//=============Checking for Collisions==============
		boolean player1Dead = box1.checkCollisions(box2.returnTrail());
		boolean player2Dead = box2.checkCollisions(box1.returnTrail());

		if(player1Dead && player2Dead){
			//System.out.println("hi");
			box1.noMove();
			box2.noMove();

			powerTaken = -1;

			roundOver = true;
		}

		else if(player1Dead){
			//System.out.println("Player 2 wins");
			box1.noMove();
			box2.noMove();

			powerTaken = -1;

			roundOver = true;
		}

		else if(player2Dead){
			//System.out.println("Player 1 wins");
			box1.noMove();
			box2.noMove();

			powerTaken = -1;

			roundOver = true;
		}

		//Power Up Timer
		tick();
	}

	//==================Power Ups====================
	//blitting random power ups
	public void tick(){
		if(powerTaken == 0){
			if(box1.turboSpeed()){
				turboTime1 -= 1;

				System.out.println(turboTime1);

				if(turboTime1 == 0){
					//box1.turboSpeed();
					box1.stopTurbo();
					turboTime1 = 5;
					//powerTaken = 0;
				}
			}

			if(box2.turboSpeed()){
				turboTime2 -= 1;

				if(turboTime2 == 0){
					box2.stopTurbo();
					turboTime2 = 0;
					//powerTaken = 0;
				}
			}

			if(objectOnScreen == false){ //if the object is not on screen
				//System.out.println(blitNewObject);
				blitNewObject -= 1;

				if(blitNewObject == 0){
					onScreenLifeTime = 500;
					objectOnScreen = true;
					blitted = false;
				}
			}

			else if(objectOnScreen == true){ //if on screen
				if(onScreenLifeTime == 0 && powerTaken == 0){
					blitNewObject = 10;
					objectOnScreen = false;
				}

				else{
					onScreenLifeTime -= 1;
				}

			}
		}	

		if(powerTaken != 0){
			onScreenLifeTime = 0;
			blitNewObject = 0;
			objectOnScreen = false;

			if(powerTaken == 1){
				powerCountDown(box1,box2);
			}

			else if(powerTaken == 2){
				powerCountDown(box2,box1);
			}

		}
	}

	public int randomPowerUp(){
		Random rand = new Random();

		int r = rand.nextInt(6);

		return r;
	}

	public void powerCountDown(Spot player, Spot otherPlayer){
		if(timeLeft > 0){
			//System.out.println(timeLeft);
			if(randPowerUp == 0){
				//System.out.println("Extra Turbo");
				player.extraTurbo();
				timeLeft = 1;
			}

			else if(randPowerUp == 1){
				//System.out.println("Portal");
				player.openPortal();
			}

			else if(randPowerUp == 2){
				//System.out.println("Double Speed");
				otherPlayer.doubleSpeed();
			}

			else if(randPowerUp == 3){
				//System.out.println("Shield");
				player.shieldUp();
			}

			else if(randPowerUp == 4){
				//System.out.println("Reverse");
				otherPlayer.move();
				otherPlayer.reverseControls();
				player.noMove();
				//otherPlayer.doubleSpeed();
			}

			else if(randPowerUp == 5){
				//System.out.println("Clear");
				player.emptyTrail();
				otherPlayer.emptyTrail();

				timeLeft = 1;
			}

			timeLeft -= 1;
		}

		else if(timeLeft == 0){
			System.out.println("Time's Up");

			powerTaken = 0;
			blitNewObject = 10;

			if(randPowerUp == 1){
				player.closePortal();
			}

			else if(randPowerUp == 2){
				player.normalSpeed();
			}

			else if(randPowerUp == 3){
				//System.out.println("Shield Down");
				player.shieldDown();
			}

			else if(randPowerUp == 4){
				otherPlayer.revertControls();
			}
		}
	}

	//===================KeyBoard======================
	//ALL THREE MUST BE PRESENT
	public void keyPressed(KeyEvent e){
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e){
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e){} //don't use this for games

	public void pMove(){
		if(roundOver == false){
			/*if(box1.returnX() == px+50 && box1.returnY() == py+210){
				box1.warpWall();
				lifeTime = 0;
			}*/

			//enter for turbo >> P1
			if(keys[KeyEvent.VK_ENTER]){
				if(box1.countTurboLeft() > 0){
					//box1.turboSpeed();

					box1.turbo();
					//box1.noTurbo();

					//turboTime1 = 5;
					//powerTaken = 1;
				}	
			}

			//tab for turbo >> p2
			if(keys[KeyEvent.VK_Q]){
				//System.out.println("HELLO");
				if(box2.countTurboLeft() > 0){
					//box1.turboSpeed();

					box2.turbo();
					//box1.noTurbo();

					//turboTime2 = 5;
					//powerTaken = 2;
				}
			}

			if(box1.controlsReversed() == false){
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
			}

			else if(box1.controlsReversed() == true){
				if(keys[KeyEvent.VK_RIGHT]){
					if(box1.moveD() != "right"){
						box1.moveLeft();
					}
				}
				else if(keys[KeyEvent.VK_LEFT]){
					if(box1.moveD() != "left"){
						box1.moveRight();
					}	
				}

				else if(keys[KeyEvent.VK_UP]){
					if(box1.moveD() != "up"){
						box1.moveDown();
					}

				}
				else if(keys[KeyEvent.VK_DOWN]){
					if(box1.moveD() != "down"){
						box1.moveRight();				
					}
				}
			}

			if(box2.controlsReversed() == false){
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

			else if(box2.controlsReversed() == true){
				if(keys[KeyEvent.VK_D]){
					if(box2.moveD() != "right"){
						box2.moveLeft();
					}
				}
				else if(keys[KeyEvent.VK_A]){
					if(box2.moveD() != "left"){
						box2.moveRight();
					}
				}
				else if(keys[KeyEvent.VK_W]){
					if(box2.moveD() != "up"){
						box2.moveDown();
					}
				}
				else if(keys[KeyEvent.VK_S]){
					if(box2.moveD() != "down"){
						box2.moveUp();
					}
				}
			}
		}

		else{
			//System.out.println("NEXT");
			if(keys[KeyEvent.VK_SPACE]){
				nextRound();
			}
		}
	}

	/*public void cMove(){ //computer moves
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
	}*/

	public void nextRound(){
		System.out.println("New Round");
		box1 = new Spot(250,470);
		box2 = new Spot(605,470);

		//System.out.println("P1 Wins: "+box1.countRoundWins());
		//System.out.println("P2 Wins: "+box2.countRoundWins());

		box1.moveRight();
		box2.moveLeft();

		objectOnScreen = false;
		blitted = true;
		pointAdded = true;
		powerTaken = 0;
		randPowerUp = 0;
		blitNewObject = 10;
		onScreenLifeTime = 0;
		roundOver = false;

		/*if(box1.countRoundWins() == 3){
			box1.winBattle();
			endBattle(box1,box2);
		}

		else if(box2.countRoundWins() == 3){
			box2.winBattle();
			endBattle(box2,box1);
		}*/
	}

	public void endBattle(Spot winner, Spot loser){
		System.out.println("END OF BATTLE");
	}
}







