//Game3.java

//import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;

public class simpleGame extends JFrame implements ActionListener{ //inherits from JFrame, JFrame and More :O
	//ACtionLIstner is a interface. BArgain- we provide the methods it wants, and it will do stuff when needed
	Timer myTimer;
	GamePanel game;

	public simpleGame(){
		super("Tron"); //calls constructor of super frame, must be first line of constructor
		setSize(900,800);
		myTimer = new Timer(30,this);
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
		new simpleGame();
	}
}

class GamePanel extends JPanel implements KeyListener{ //Keyboard is an interface
	//private int boxx1, boxy1, boxx2, boxy2, dx, dy;
	private boolean [] keys;
	private Spot box1;
	private Spot box2;

	public GamePanel(){
		box1 = new Spot(650,400);
		box2 = new Spot(350,400);

		box1.moveLeft();
		box2.moveRight();

		setFocusable(true);
		addKeyListener(this);
		requestFocus();

		keys = new boolean[KeyEvent.KEY_LAST+1];
	}

	@Override //think I am overriding one of the methods in JPanel
	public void paintComponent(Graphics g){ //supply same parameters that replaces it
		g.setColor(new Color(222,255,222));
		g.fillRect(0,0,getWidth(),getHeight());
		g.setColor(Color.blue);
		g.fillRect(box1.getX(),box1.getY(),10,10);

		g.setColor(Color.red);
		g.fillRect(box2.getX(),box2.getY(),10,10);
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
		box1.move();
		box2.move();
	}

	public void pOneMove(){
		if(box1.hitBorder() == false){
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
	}

	public void pTwoMove(){
		if(box2.hitBorder() == false){
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
	}
}







