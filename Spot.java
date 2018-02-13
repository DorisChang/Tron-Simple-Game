//Spot.java

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;

public class Spot{
	//private ArrayList<int> path = new ArrayList<int>();
	private int cx, cy; //current value;
	private int xChange, yChange;
	private Boolean hit,pressTurbo;
	
	
	public Spot(int x, int y){
		cx = x;
		cy = y;
		
		hit = false;
		pressTurbo = false;
	}
	
	public Point returnPos(){
		return(new Point(cx,cy));
		}
	
	public Point returnNextPos(){
		return(new Point(cx+xChange,cy+yChange));
		}
		
	public boolean hitBorder(){
		//System.out.println("XCHANGE: "+xChange);
		//System.out.println("YCHANGE: "+yChange);

		if(cx <= 50 || cx >= 840){
			noMove();
			hit = true;
			return true;
		}

		else if(cy <= 210 || cy >= 720){
			noMove();
			hit = true;
			return true;
		}

		else{
			hit = false;
			return false;
		}
	}

	public int getX(){
		return cx;
	}

	public int getY(){
		return cy;
	}

	public void noMove(){
		xChange = 0;
		yChange = 0;
	}

	public boolean getHit(){
		return hit;
	}

	public void hitObject(){
		hit = true;
	}

	public void move(){
		hitBorder();
		cx += xChange;
		cy += yChange;

		if(pressTurbo == true){
			xChange /= 5;
			yChange /= 5;
			pressTurbo = false;
		}


		//System.out.println(cx);
	}

	public void turbo(){
		pressTurbo = true;
		xChange *= 5;
		yChange *= 5;
	}

	public void moveRight(){
		xChange = 10;
		yChange = 0;
	}

	public void moveLeft(){
		xChange = -10;
		yChange = 0;
	}

	public void moveUp(){
		yChange = -10;
		xChange = 0;
	}

	public void moveDown(){
		yChange = 10;
		xChange = 0;
	}
}