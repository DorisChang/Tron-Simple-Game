//simpleGame.java
//Minya Bai & Doris Chang
//Tron LightCycles
//This game allows you to control a little cycle that builds a wall after itself. The object of the 
//game is to maneuver your opponent so that they run into a wall before you do.

import java.util.Random;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;

public class simpleGame extends JFrame implements ActionListener{
	
	Timer myTimer;
	Timer powerUpTimer;
	GamePanel game;

	public simpleGame(){
		super("Tron"); 
		setSize(855,800);
		myTimer = new Timer(40,this);
		game = new GamePanel();
		add(game);

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new GameMenu(this);
	}

	public void actionPerformed(ActionEvent e){
		if(game != null){
			game.twoPlayer();
			game.repaint();
		}
	}
	
	public void instructions(){ //used for the instructions screen 
		new InstructMenu(this);
		}
	
	public void start(){ //used to start the game
 		myTimer.start();
 		setVisible(true);
 		}

	public static void main(String[]args){
		new simpleGame();
	}
}

class GameMenu extends JFrame implements KeyListener{ //JFrame for the game/main menu, uses SPACE to continue 
 	private simpleGame menu; 
 	private boolean [] keys;
 	
 	public GameMenu(simpleGame m){
 		super("game menu");
 		setSize(850,800);
 		menu = m;
 		
 		ImageIcon back = new ImageIcon("images/titleScreen.png"); 
 		JLabel backLabel = new JLabel(back);
 		JLayeredPane mPage=new JLayeredPane(); 	
 		mPage.setLayout(null);
 		
 		backLabel.setSize(855,800);
 		backLabel.setLocation(0,0);
 		mPage.add(backLabel,1);				
 			
 		add(mPage);
 		addKeyListener(this);
 		setVisible(true);
 		keys = new boolean[KeyEvent.KEY_LAST+1];
 		}
    
    public void keyPressed(KeyEvent e){
		keys[e.getKeyCode()] = true;
			
		if(keys[KeyEvent.VK_SPACE]){ //when SPACE is pressed
			menu.instructions(); //show the instructions 
			setVisible(false);	
			}

		}
	public void keyReleased(KeyEvent e){
		}
	public void keyTyped(KeyEvent e){ 
		}
 	}
 	
class InstructMenu extends JFrame implements KeyListener{ //the instructions menu, uses SPACE to continue
	private simpleGame bg;
 	private boolean [] keys;
 	
 	public InstructMenu(simpleGame m){
 		super("instructions");
 		setSize(850,800);
 		bg = m;
 		
 		ImageIcon back = new ImageIcon("images/Instructions.png");
 		JLabel backLabel = new JLabel(back);
 		JLayeredPane mPage=new JLayeredPane(); 
 		mPage.setLayout(null);
 		
 		backLabel.setSize(855,800);
 		backLabel.setLocation(0,0);
 		mPage.add(backLabel,1);					
 			
 		add(mPage);
 		addKeyListener(this);
 		setVisible(true);
 		keys = new boolean[KeyEvent.KEY_LAST+1];
 		}
 	
 	public void keyPressed(KeyEvent e){
		keys[e.getKeyCode()] = true;
			
		if(keys[KeyEvent.VK_SPACE]){ //when SPACE is pressed
			bg.start(); //starts the game
			setVisible(false);	
			}
		}
		
	public void keyReleased(KeyEvent e){
		}
	public void keyTyped(KeyEvent e){ 
		}
	}


class GamePanel extends JPanel implements KeyListener{  
	private boolean [] keys;
	private Spot box1;
	private Spot box2;
	private Image back, btw, rOver;

	private int p1points, p2points, p1matches, p2matches; //points from 0-3 during each match (3 ends the game), the number of matches won
	private boolean p1wins = false, p2wins = false; //boolean for keeping track of whether the player should get a point
	private boolean pointAdded; //boolean to keep prevent more than one point from being rewarded for the same action

	private Rectangle p;
	private int px = 0, py = 0;
	private int onScreenLifeTime = 500;
	private int blitNewObject = 10;
	private int timeLeft = 140;
	private int turboTime1 = 5, turboTime2 = 5;
	private boolean objectOnScreen, blitted, roundOver, pressedSpace;
	private int powerTaken; //0 for not taken, 1 for player 1, 2 for player 2
	private int randPowerUp; 
	private int messageNum = 100; //index for the message displayed at the end of a round (100 when no new random number for messageNum has be generated)
	private String message; //the actual message displayed at the end of a round, determined randomly
	private int usingPowerNum = 100; //index for the power up being used (100 when not applicable)
	
	private String[] powerUps = {"   Extra Turbo","        Portal","      2X Speed","        Shield","Reverse Controls","Clear All Trails"}; //titles displayed when a player obtains a power up
	private String[] messages1 = {"    Easy win!", "No competition!"}; //selection of messages for the end of a round
	private String[] messages2 = {"That was close!","  Close one!"," Super close!"}; //selection of messages for the end of a tight round

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
		back = new ImageIcon("images/GameScreen.png").getImage(); //background of the game when playing
		btw = new ImageIcon("images/EndMatch.png").getImage(); //the end of a match screen used in between matches, shows the point score for that match and the match score
		rOver = new ImageIcon("images/EndRoundSlip.png").getImage(); //image used to tell players to press enter to continue the round
		p = new Rectangle(0,0,10,10);
		
		p1matches = 0;
		p2matches = 0;
		
		p1points = 0;
		p2points = 0;

		setFocusable(true);
		addKeyListener(this);
		requestFocus();

		keys = new boolean[KeyEvent.KEY_LAST+1];
	}

	//==============Drawing Stuff==============
	@Override 
	public void paintComponent(Graphics g){ 
			
		if(p1points == 3 || p2points == 3){ //if someone wins the match
			g.drawImage(btw,0,0,this); 
			g.setColor(new Color(250,250,250));
			g.setFont(new Font("Calibri",Font.PLAIN,32));
			
			g.drawString(""+p1matches, 360,630); //displays the match score
			g.drawString("-",420,630);
			g.drawString(""+p2matches, 480,630);
			
			Random rand = new Random(); //randomly chooses the message for the match
			if(Math.abs(p1points - p2points) > 1 && messageNum == 100){ //when the scores were not close
				messageNum = rand.nextInt(messages1.length);
				message = messages1[messageNum];
				}
				
			else if(Math.abs(p1points - p2points) == 1 && messageNum == 100){ //when it was a tight match
				messageNum = rand.nextInt(messages2.length);
				message = messages2[messageNum];
				}
				
			g.drawString(message,340,400);
			
			g.drawString(""+p1points,360,330); //displays the score of the match that just ended
			g.drawString("-",420,330);
			g.drawString(""+p2points,480,330);
			}

		
		else{ //during the game
			g.drawImage(back,0,0,this);
			
			if(usingPowerNum != 100){ //when a power up is being used
				g.setColor(new Color(250,250,250));
				g.setFont(new Font("Calibri",Font.PLAIN,24));
				g.drawString(powerUps[usingPowerNum], 353,95); //displays the power up title on the top
					}
			
			//Power Up Timer
			g.fillRect(108,30,10,75);
			g.fillRect(741,30,10,75);
	
			//=====Rounds Won Bar====
			//Number Square
			g.setColor(new Color(0,0,0));
			g.setFont(new Font("Calibri",Font.PLAIN,32));
			g.drawString(""+p1points, 253,72);
			g.drawString(""+p2points, 588,72);
			//g.setColor(new Color(17,122,72));
	
			//========Trails========
			ArrayList<Rectangle>aTrail = box1.returnTrail();
			g.setColor(new Color(65,227,204));
	
			//Player 1 Trail
			for(int i = 0; i < aTrail.size(); i++){
				Rectangle trail = aTrail.get(i);
				g.fillRect((int)trail.getX(),(int)trail.getY(),(int)trail.getWidth(),(int)trail.getHeight());
			}
	
			//turbo left
			for(int i = 0; i < box1.countTurboLeft(); i++){
				g.fillRect(149+29*i,83,16,16);
			}
	
			//p1 win bar
			for(int i = 0; i < p1points; i++){
				g.fillRect(297+39*i,56,37,3);
			}
	
			//time left
			//g.setColor(new Color(100,0,0));
			//g.fillRect(110,30,6,70);
	
			//time gone
			//g.setColor(Color.red);
			if(powerTaken == 1){
				g.fillRect(110,32+(140-timeLeft)/2,6,70-(140-timeLeft)/2);
				//System.out.println(timeLeft);
			}
			
		
			//turbo counters
			//g.setColor(new Color(100,0,0));
			//for(int i = 3 - box1.countTurboLeft(); i > 0; i--){
				//g.fillRect(215-25*i,70,20,20);
			//}
	
			//empty win counter
			//for(int i = 3-p1points; i > 0; i--){
				//g.fillRect(414-39*i,56,37,3);
			//}
	
			//Player 2 Trail
			ArrayList<Rectangle>bTrail = box2.returnTrail();
			g.setColor(new Color(130,101,182));
	
			for(int i = 0; i < bTrail.size(); i++){
				Rectangle trail2 = bTrail.get(i);
				g.fillRect((int)trail2.getX(),(int)trail2.getY(),(int)trail2.getWidth(),(int)trail2.getHeight());
			}
	
			//turbo left
			for(int i = 0; i < box2.countTurboLeft(); i++){
				g.fillRect(642+29*i,83,16,16);
			}
	
			//p2 wins bar
			for(int i = 0; i < p2points; i++){
				g.fillRect(441+39*i,56,37,3);
			}
	
			//time gone
			//g.setColor(new Color(0,0,100));
			//g.fillRect(743,30,6,70);
	
			//time left
			//g.setColor(Color.blue);
			if(powerTaken == 2){
				g.fillRect(743,32+(140-timeLeft)/2,6,70-(140-timeLeft)/2);
				//System.out.println(timeLeft);
			}
			
			
			
			//empty turbo counter
			//g.setColor(new Color(0,0,100));
			//for(int i = 3 - box2.countTurboLeft(); i > 0; i--){
				//g.fillRect(730-25*i,70,20,20);
			//}
	
			//empty win bar
			//for(int i = 3-p2points; i > 0; i--){
				//g.fillRect(558-39*i,56,37,3);
			//}
	
			//Players (Ovals)
			g.setColor(new Color(200,200,200));
			g.fillOval(box1.returnX()-1,box1.returnY()-1,6,6);
			g.fillOval(box2.returnX()-1,box2.returnY()-1,6,6);
	
			g.setColor(new Color(65,227,204));
			g.fillOval(box1.returnX(),box1.returnY(),4,4);
	
			g.setColor(new Color(110,98,150));
			g.fillOval(box2.returnX(),box2.returnY(),4,4);
	
			//Colliding Rectangles (Testing Only - Delete afterwards)
			Rectangle rect1 = box1.drawR();
			Rectangle rect2 = box2.drawR();
	
			g.setColor(new Color(255,255,0));
			g.drawRect((int)rect1.getX(),(int)rect1.getY(),(int)rect1.getWidth(),(int)rect1.getHeight());
	
			g.setColor(new Color(0,255,255));
			g.drawRect((int)rect2.getX(),(int)rect2.getY(),(int)rect2.getWidth(),(int)rect2.getHeight());
			

			
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
			
			if(roundOver){ //changes the text at the bottom of the screen (tells players to press space to continue)
				g.drawImage(rOver,0,710,this);
				}
		}

		
	}

	//===================Moving Players==================
	public void twoPlayer(){
		pMove();
		
		box1.move();
		box2.move();


		//System.out.println(box1.returnX());

		//box1.noMove();

		/*if(box1.turboInitiated()){
			box1.turbo();
			box1.noTurbo();

			System.out.println("Turbo Time");

			turboTime = 3;
			powerTaken = 3;
		}*/

		if(box1.getPowerUp(p)){
			//System.out.println("I got it!");
			usingPowerNum = randPowerUp; //updates to the power up that has been obtained
			
			if(randPowerUp != 2 && randPowerUp != 4){
				powerTaken = 1;
				objectOnScreen = false;
				timeLeft = 140;
			}

			else{
				powerTaken = 2;
				objectOnScreen = false;
				timeLeft = 140;
			}
		}

		if(box2.getPowerUp(p)){
			//System.out.println("I got it!");
			usingPowerNum = randPowerUp; //updates to the power up that has been obtained
			
			if(randPowerUp != 2 && randPowerUp != 4){
				//box2.noMove();
				//box1.move();
				powerTaken = 2;
				objectOnScreen = false;
				timeLeft = 140;
			}

			else{
				powerTaken = 1;
				objectOnScreen = false;
				timeLeft = 140;
			}
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
			p2wins = true;
			powerTaken = -1;
			roundOver = true;
		}

		else if(player2Dead){
			//System.out.println("Player 1 wins");
			box1.noMove();
			box2.noMove();
			p1wins = true;
			powerTaken = -1;
			roundOver = true;
		}
		
		
		if(p1points < 3 && pointAdded == false && p1wins){ //if player 1 should get a point (has not yet won the round and the point hasn't been added already)
			p1points += 1;
			p1wins = false; 
			pointAdded = true;
			if(p1points == 3){ //if p1 wins the match after the added point
				p1matches += 1;
			}
		}
			
		if(p2points < 3 && pointAdded == false && p2wins){ //if player 2 should get a point (has not yet won the round and the point hasn't been added already)
			p2points += 1;
			p2wins = false;
			pointAdded = true;
			if(p2points == 3){ //if p2 wins the match after the added point
				p2matches += 1;
			}
		}
		
	

		//System.out.println(box1.controlsReversed());
		//System.out.println(box2.controlsReversed());
			
		//Power Up Timer
		tick();
	}

	//==================Power Ups====================
	//blitting random power ups
	public void tick(){
		if(powerTaken == 0){
			if(box1.turboSpeed()){
				turboTime1 -= 1;

				//System.out.println(turboTime2);

				if(turboTime1 == 0){
					//box1.turboSpeed();
					box1.stopTurbo();
					//turboTime1 = 5;
					//powerTaken = 0;
				}
			}

			if(box2.turboSpeed()){
				turboTime2 -= 1;

				

				if(turboTime2 == 0){
					box2.stopTurbo();
					//turboTime2 = 5;
					//powerTaken = 0;

					//System.out.println(turboTime2);
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

		if(powerTaken != 0 && (box1.turboSpeed() == false || box2.turboSpeed() == false)){
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
				timeLeft = 0;
			}

			else if(randPowerUp == 1){
				//System.out.println("Portal");
				player.openPortal();
			}

			else if(randPowerUp == 2){
				//System.out.println("Double Speed");
				player.doubleSpeed();
			}

			else if(randPowerUp == 3){
				//System.out.println("Shield");
				player.shieldUp();
			}

			else if(randPowerUp == 4){
				//System.out.println("Reverse");
				//otherPlayer.move();
				player.reverseControls();
				//player.noMove();
				//otherPlayer.doubleSpeed();
			}

			else if(randPowerUp == 5){
				//System.out.println("Clear");
				player.emptyTrail();
				otherPlayer.emptyTrail();

				timeLeft = 0;
			}

			timeLeft -= 1;
		}

		else if(timeLeft <= 0){
			System.out.println("Time's Up");
			usingPowerNum = 100; 
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
				if(box2.countTurboLeft() > 0){
					//box1.turboSpeed();

					box2.turbo();
					//System.out.println(roundOver);
					//box1.noTurbo();

					turboTime2 = 5;
					//powerTaken = 2;


				}	
			}

			//tab for turbo >> p2
			if(keys[KeyEvent.VK_Q]){
				//System.out.println("HELLO");
				if(box1.countTurboLeft() > 0){
					//box1.turboSpeed();

					box1.turbo();
					//box1.noTurbo();

					turboTime1 = 5;
					//powerTaken = 1;
				}
			}

			if(box2.controlsReversed() == false){
				if(keys[KeyEvent.VK_RIGHT]){
					if(box2.moveD() != "left"){
						box2.moveRight();
					}
				}
				else if(keys[KeyEvent.VK_LEFT]){
					if(box2.moveD() != "right"){
						box2.moveLeft();
					}	
				}

				else if(keys[KeyEvent.VK_UP]){
					if(box2.moveD() != "down"){
						box2.moveUp();
					}

				}
				else if(keys[KeyEvent.VK_DOWN]){
					if(box2.moveD() != "up"){
						box2.moveDown();				
					}
				}
			}

			else if(box2.controlsReversed() == true){
				if(keys[KeyEvent.VK_RIGHT]){
					if(box2.moveD() != "right"){
						box2.moveLeft();
					}
				}
				else if(keys[KeyEvent.VK_LEFT]){
					if(box2.moveD() != "left"){
						box2.moveRight();
					}	
				}

				else if(keys[KeyEvent.VK_UP]){
					if(box2.moveD() != "up"){
						box2.moveDown();
					}

				}
				else if(keys[KeyEvent.VK_DOWN]){
					if(box2.moveD() != "down"){
						box2.moveRight();				
					}
				}
			}

			if(box1.controlsReversed() == false){
				if(keys[KeyEvent.VK_D]){
					if(box1.moveD() != "left"){
						box1.moveRight();
					}
				}
				else if(keys[KeyEvent.VK_A]){
					if(box1.moveD() != "right"){
						box1.moveLeft();
					}
				}
				else if(keys[KeyEvent.VK_W]){
					if(box1.moveD() != "down"){
						box1.moveUp();
					}
				}
				else if(keys[KeyEvent.VK_S]){
					if(box1.moveD() != "up"){
						box1.moveDown();
					}
				}
			}

			else if(box1.controlsReversed() == true){
				if(keys[KeyEvent.VK_D]){
					if(box1.moveD() != "right"){
						box1.moveLeft();
					}
				}
				else if(keys[KeyEvent.VK_A]){
					if(box1.moveD() != "left"){
						box1.moveRight();
					}
				}
				else if(keys[KeyEvent.VK_W]){
					if(box1.moveD() != "up"){
						box1.moveDown();
					}
				}
				else if(keys[KeyEvent.VK_S]){
					if(box1.moveD() != "down"){
						box1.moveUp();
					}
				}
			}
		}

		else{
			//System.out.println("NEXT");
			if(keys[KeyEvent.VK_SPACE]){
				pressedSpace = true;
				if(p1points == 3){
					p1points = 0;
					p2points = 0;
					messageNum = 100;
				}

				if(p2points ==3){
					p1points = 0;
					p2points = 0;
					messageNum = 100;
				}

				p1wins = false;
				p2wins = false;
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
		//pointAdded = true;
		pointAdded = false;
		powerTaken = 0;
		randPowerUp = 0;
		blitNewObject = 10;
		onScreenLifeTime = 0;
		roundOver = false;
		pressedSpace = false;

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







