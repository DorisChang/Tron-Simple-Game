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

	//=======Power Ups======
	private Rectangle p; //power up rectangle
	private int px = 0, py = 0;
	private int onScreenLifeTime = 200;
	private int blitNewObject = 350;
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
	0 - extra turbo (gains one turbo)
	1 - portal (allows you to travel from one side of the playing field to the other (side walls don't affect you)
	2 - double speed (doubles the speed of the opponent)
	3 - shield (protects you from the wall BUILT; borders still hurt)
	4 - reverse opp. controls
	5 - clear trails

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
			//g.fillRect(108,30,10,75);
			//g.fillRect(741,30,10,75);
	
			//=====Rounds Won Bar====
			//Number Square
			g.setColor(new Color(0,0,0));
			g.setFont(new Font("Calibri",Font.PLAIN,32));
			g.drawString(""+p1points, 253,72);
			g.drawString(""+p2points, 588,72);
	
			//========Trails========
			ArrayList<Rectangle>aTrail = box1.returnTrail();
			g.setColor(new Color(65,227,204));
	
			//PLAYER 1
			//trail
			for(int i = 0; i < aTrail.size(); i++){
				Rectangle trail = aTrail.get(i);
				g.fillRect((int)trail.getX(),(int)trail.getY(),(int)trail.getWidth(),(int)trail.getHeight());
			}
	
			//# of turbo left (squares)
			for(int i = 0; i < box1.countTurboLeft(); i++){
				g.fillRect(149+29*i,83,16,16);
			}
	
			//# of wins (bar)
			for(int i = 0; i < p1points; i++){
				g.fillRect(297+39*i,56,37,3);
			}

			//time remaining for power up (bar)
			if(powerTaken == 1){
				g.fillRect(110,32+(140-timeLeft)/2,6,70-(140-timeLeft)/2);
			}
	
			//PLAYER 2 
			//trail
			ArrayList<Rectangle>bTrail = box2.returnTrail();
			g.setColor(new Color(130,101,182));
	
			for(int i = 0; i < bTrail.size(); i++){
				Rectangle trail2 = bTrail.get(i);
				g.fillRect((int)trail2.getX(),(int)trail2.getY(),(int)trail2.getWidth(),(int)trail2.getHeight());
			}
	
			//# of turbo left (squares)
			for(int i = 0; i < box2.countTurboLeft(); i++){
				g.fillRect(642+29*i,83,16,16);
			}
	
			//# of wins (bar)
			for(int i = 0; i < p2points; i++){
				g.fillRect(441+39*i,56,37,3);
			}
	
			//time remaining for power up (bar)
			if(powerTaken == 2){
				g.fillRect(743,32+(140-timeLeft)/2,6,70-(140-timeLeft)/2);
				//System.out.println(timeLeft);
			}
	
			//Players (Circle - Ring)
			g.setColor(new Color(200,200,200));
			g.fillOval(box1.returnX()-1,box1.returnY()-1,6,6);
			g.fillOval(box2.returnX()-1,box2.returnY()-1,6,6);
	
			g.setColor(new Color(65,227,204));
			g.fillOval(box1.returnX(),box1.returnY(),4,4);
	
			g.setColor(new Color(110,98,150));
			g.fillOval(box2.returnX(),box2.returnY(),4,4);
			
			//========Power Ups========
			if(objectOnScreen == true && powerTaken == 0){ //if on screen
				//choose random power up
				if(blitted == false){ 
					randPowerUp = randomPowerUp(); 
	
					Random rand = new Random();
	
					px = rand.nextInt(720); //random x-coord
					py = rand.nextInt(590); //random y-coord
	
					p.setLocation(px+60,py+110);
	
					blitted = true;
				}
	
				//set color depending on what power up
				if(randPowerUp == 0){
					//Extra Turbo
					g.setColor(new Color(255,255,0));
				}
	
				if(randPowerUp == 1){
					//Turbo
					g.setColor(new Color(0,255,255));
				}
	
				if(randPowerUp == 2){
					//Double Speed
					g.setColor(new Color(255,0,255));
				}
	
				if(randPowerUp == 3){
					//Shield
					g.setColor(new Color(255,255,255));
				}
	
				if(randPowerUp == 4){
					//Reverse Controls
					g.setColor(new Color(255,140,0));
				}
	
				if(randPowerUp == 5){
					//Clear
					g.setColor(new Color(0,255,0));
				}
	
				g.fillRect(px+60,py+110,10,10);
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

		//===Getting Power Ups===
		if(box1.getPowerUp(p)){
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
			usingPowerNum = randPowerUp; //updates to the power up that has been obtained
			
			if(randPowerUp != 2 && randPowerUp != 4){
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

		//=============Checking for Collisions==============
		boolean player1Dead = box1.checkCollisions(box2.returnTrail());
		boolean player2Dead = box2.checkCollisions(box1.returnTrail());

		if(player1Dead && player2Dead){
			box1.noMove();
			box2.noMove();

			powerTaken = -1;
			roundOver = true;
		}

		else if(player1Dead){
			box1.noMove();
			box2.noMove();
			p2wins = true;
			powerTaken = -1;
			roundOver = true;
		}

		else if(player2Dead){
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
			
		//Power Up Timer
		tick();
	}

	//==================Power Ups====================
	//blitting random power ups
	public void tick(){
		if(powerTaken == 0){ //if no one has yet to grab a power up
			//Turbo
			if(box1.turboSpeed()){
				turboTime1 -= 1;

				if(turboTime1 == 0){
					box1.stopTurbo();
				}
			}

			if(box2.turboSpeed()){
				turboTime2 -= 1;

				if(turboTime2 == 0){
					box2.stopTurbo();
				}
			}

			if(objectOnScreen == false){ //if the object is not on screen
				blitNewObject -= 1;

				if(blitNewObject == 0){
					onScreenLifeTime = 200;
					objectOnScreen = true;
					blitted = false;
				}
			}

			else if(objectOnScreen == true){ //if on screen
				if(onScreenLifeTime == 0 && powerTaken == 0){
					blitNewObject = 300;
					objectOnScreen = false;
				}

				else{
					onScreenLifeTime -= 1;
				}

			}
		}	

		//Power up (not Turbo)
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

	public int randomPowerUp(){ //choosing a random power up
		Random rand = new Random();

		int r = rand.nextInt(6);

		return r;
	}

	//Power Ups
	public void powerCountDown(Spot player, Spot otherPlayer){
		if(timeLeft > 0){
			if(randPowerUp == 0){
				player.extraTurbo();
				timeLeft = 0;
			}

			else if(randPowerUp == 1){
				player.openPortal();
			}

			else if(randPowerUp == 2){
				player.doubleSpeed();
			}

			else if(randPowerUp == 3){
				player.shieldUp();
			}

			else if(randPowerUp == 4){
				player.reverseControls();
			}

			else if(randPowerUp == 5){
				player.emptyTrail();
				otherPlayer.emptyTrail();

				timeLeft = 0;
			}

			timeLeft -= 1;
		}

		else if(timeLeft <= 0){ //time's up
			usingPowerNum = 100; 
			powerTaken = 0;
			blitNewObject = 300;

			if(randPowerUp == 1){
				player.closePortal();
			}

			else if(randPowerUp == 2){
				player.normalSpeed();
			}

			else if(randPowerUp == 3){
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
			//enter for turbo >> P1
			if(keys[KeyEvent.VK_ENTER]){
				if(box2.countTurboLeft() > 0){
					box2.turbo();

					turboTime2 = 5;
				}	
			}

			//tab for turbo >> p2
			if(keys[KeyEvent.VK_Q]){
				if(box1.countTurboLeft() > 0){
					box1.turbo();

					turboTime1 = 5;
				}
			}

			//Normal Controls
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

			//Reversed Controls
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

	public void nextRound(){
		//initial location and direction
		box1 = new Spot(250,470);
		box2 = new Spot(605,470);

		box1.moveRight();
		box2.moveLeft();

		objectOnScreen = false;
		blitted = true;
		pointAdded = false;
		powerTaken = 0;
		usingPowerNum = 100;
		randPowerUp = 0;
		blitNewObject = 300;
		onScreenLifeTime = 0;
		roundOver = false;
		pressedSpace = false;
	}
}







