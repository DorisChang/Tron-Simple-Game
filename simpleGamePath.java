//Game3.java

//HOT DIGGITY DOG!

//import java.util.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.lang.Math;
//import java.awt.geom.*;


public class simpleGamePath extends JFrame implements ActionListener{ //inherits from JFrame, JFrame and More :O
	//ActionListner is a interface. Bargain- we provide the methods it wants, and it will do stuff when needed
	Timer myTimer;
	GamePanel game;
	
	//public static ArrayList<Point>pOnePath = new ArrayList<Point>();
	//public static ArrayList<Point>pTwoPath = new ArrayList<Point>();

	public simpleGamePath(){
		super("Tron"); //calls constructor of super frame, must be first line of constructor
		
		setSize(900,800);
		myTimer = new Timer(100,this);
		myTimer.start();
		game = new GamePanel();
		add(game);

		setResizable(false);
		setVisible(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e){
		if(game != null){
			game.moving();
			game.pOneMove();
			game.pTwoMove();
			game.repaint();
		}
	}
	public static void main(String[]args){
		new simpleGamePath();
	}
}

class GamePanel extends JPanel implements KeyListener{ //Keyboard is an interface
	//private int boxx1, boxy1, boxx2, boxy2, dx, dy;
	private boolean [] keys;
	private Spot box1;
	private Spot box2;
	private Boolean newRound = false;
	private static ArrayList<Point>pOnePath = new ArrayList<Point>();
	private static ArrayList<Point>pTwoPath = new ArrayList<Point>();
	
	private int p1_Points, p2_Points;
	//private ArrayList<Trail>path = new ArrayList<Trail>();

	//pOnePath.add(new Point(box1.getX(),box1.getY()));
	//pTwoPath.add(new Point(box2.getX(),box2.getY()));
	
	public GamePanel(){
		newRound();

		p1_Points = 0;
		p2_Points = 0;

		setFocusable(true);
		addKeyListener(this);
		requestFocus();

		keys = new boolean[KeyEvent.KEY_LAST+1];
	}

	@Override //think I am overriding one of the methods in JPanel
	public void paintComponent(Graphics g){ //supply same parameters that replaces it
		g.setColor(new Color(222,255,222));
		//g.fillRect(0,0,getWidth(),getHeight());

		g.setColor(new Color(125,125,125));
		if(newRound){
			g.fillRect(50,210,800,520);	
			newRound = false;
			}
		

		g.setColor(new Color(200,200,200));
		g.fillRect(50,40,125,125);
		g.fillRect(725,40,125,125);

		g.setColor(new Color(200,200,200));
		g.fillRect(0,0,5,50);

		g.setColor(Color.blue);
		g.fillRect(box1.getX(),box1.getY(),10,10);

		g.setColor(Color.red);
		g.fillRect(box2.getX(),box2.getY(),10,10);
	}
	
	public boolean oHitT(){ //player one hit player two's path, or it's own path
		
		if(pTwoPath.contains(box1.returnNextPos())){ //hit the other player's path
			return true;
			}
		if(pOnePath.contains(box1.returnNextPos())){
			return true;
			}
		
		/*for(int i=0;i<box1.returnNextPos().x-box1.returnPos().x;i++){
			pOnePath.add()
			}*/
		if(box1.returnNextPos().x-box1.returnPos().x>0){ //moving right
			for(int i=0; i<box1.returnNextPos().x-box1.returnPos().x; i++){
				pOnePath.add(new Point(box1.returnPos().x+i,box1.returnPos().y));
				}
			}
		else{ //moving left
			for(int i=0; i<Math.abs(box1.returnNextPos().x-box1.returnPos().x); i++){
				pOnePath.add(new Point(box1.returnPos().x-i,box1.returnPos().y));
				}
			}
		if(box1.returnNextPos().y-box1.returnPos().y>0){ //moving down
			for(int i=0; i<box1.returnNextPos().y-box1.returnPos().y; i++){
				pOnePath.add(new Point(box1.returnPos().x,box1.returnPos().y+i));
				}
			}
		else{ //moving up
			for(int i=0; i<Math.abs(box1.returnNextPos().y-box1.returnPos().y); i++){
				pOnePath.add(new Point(box1.returnPos().x,box1.returnPos().y-i));
				}
			}
			
		//pOnePath.add(box1.returnNextPos()); //doesn't hit the other player's path
		return false;
		}
	
	public boolean tHitO(){ //player two hit player one's path, or it's own path
		
		if(pOnePath.contains(box2.returnNextPos())){
			return true;
			}
			
		if(pTwoPath.contains(box2.returnNextPos())){
			return true;
			}
			
		//pTwoPath.add(box2.returnNextPos());
		if(box2.returnNextPos().x-box2.returnPos().x>0){ //moving right
			for(int i=0; i<box2.returnNextPos().x-box2.returnPos().x; i++){
				pTwoPath.add(new Point(box2.returnPos().x+i,box2.returnPos().y));
				}
			}
		else{ //moving left
			for(int i=0; i<Math.abs(box2.returnNextPos().x-box2.returnPos().x); i++){
				pTwoPath.add(new Point(box2.returnPos().x-i,box2.returnPos().y));
				}
			}
		if(box2.returnNextPos().y-box2.returnPos().y>0){ //moving down
			for(int i=0; i<box2.returnNextPos().y-box2.returnPos().y; i++){
				pTwoPath.add(new Point(box2.returnPos().x,box2.returnPos().y+i));
				}
			}
		else{ //moving up
			for(int i=0; i<Math.abs(box2.returnNextPos().y-box2.returnPos().y); i++){
				pTwoPath.add(new Point(box2.returnPos().x,box2.returnPos().y-i));
				}
			}	
		return false;
		}
	
	//ALL THREE MUST BE PRESENT
	public void keyPressed(KeyEvent e){
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e){
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e){} //don't use this for games

	public void moving(){
		System.out.println(pOnePath.size());
		System.out.println(pTwoPath.size());
		
		if(oHitT()){
			System.out.println("Player one lost");
			newRound();
			}
		else{
			box1.move();
			}
		if(tHitO()){
			System.out.println("Player two lost");
			newRound();
			}
		else{
			box2.move();
			}
		
		
	}
	
	public void pOneMove(){
		if(box1.getHit() == false && box2.getHit() == false){
			if(keys[KeyEvent.VK_ENTER]){
				box1.turbo();
			}

			if(keys[KeyEvent.VK_RIGHT]){
				box1.moveRight();
			}
			else if(keys[KeyEvent.VK_LEFT]){
				box1.moveLeft();
			}
			else if(keys[KeyEvent.VK_UP]){
				box1.moveUp();
			}
			else if(keys[KeyEvent.VK_DOWN]){
				box1.moveDown();
			}
		}

		else{
			box1.noMove();

			if(keys[KeyEvent.VK_SPACE]){
				newRound();
			}
		}
	}

	public void pTwoMove(){
		if(box2.getHit() == false && box1.getHit() == false){
			if(keys[KeyEvent.VK_D]){
				box2.moveRight();
			}
			else if(keys[KeyEvent.VK_A]){
				box2.moveLeft();
			}
			else if(keys[KeyEvent.VK_W]){
				box2.moveUp();
			}
			else if(keys[KeyEvent.VK_S]){
				box2.moveDown();
			}
		}

		else{
			box2.noMove();
			
			if(keys[KeyEvent.VK_SPACE]){
				newRound();
			}
		}
	}

	public void newRound(){
		System.out.println("New Round");
		box2 = new Spot(350,400);
		box1 = new Spot(650,400);
		pOnePath.clear();
		pTwoPath.clear();
		//ArrayList<Point>pOnePath = new ArrayList<Point>();
		//ArrayList<Point>pTwoPath = new ArrayList<Point>();
		newRound=true;
		box2.moveRight();
		box1.moveLeft();
	}
}
