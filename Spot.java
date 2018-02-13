//Spot.java

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;

public class Spot{
	//private ArrayList<int> path = new ArrayList<int>();
	/*public static final int UP = 1;
	public static final int DOWN = -1;
	public static final int RIGHT = 2;
	public static final int LEFT = -2;*/

	private int cx, cy, w, h; //current value;
	private int xChange, yChange;
	private Boolean hit,pressTurbo;
	private int points;
	private String direction;
	private boolean wallWarp;

	public Spot(int x, int y){
		cx = x;
		cy = y;

		hit = false;
		pressTurbo = false;

		points = 0;

		wallWarp = false;
	}

	public boolean hitBorder(){
		//System.out.println("XCHANGE: "+xChange);
		//System.out.println("YCHANGE: "+yChange);

		if(wallWarp == true){
			if(cx < 50){
				cx = 790;
			}

			else if(cx > 790){
				cx = 50;
			}

			else if(cy < 210){
				cy = 720;
			}

			else if(cy > 720){
				cy = 210;
			}

			return false;
		}

		else{
			if(cx <= 50 || cx >= 790){
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
	}

	public int getX(){
		return cx;
	}

	public int getY(){
		return cy;
	}

	public int width(){
		return w;
	}

	public int height(){
		return h;
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

	public String moveD(){
		return direction;
	}

	public void move(){
		hitBorder();
		cx += xChange;
		cy += yChange;

		if(xChange < 0){
			direction = "left";
			w += xChange;
		}
		else if(xChange > 0){
			direction = "right";
			w += xChange;
		}
		else if(yChange < 0){
			direction = "up";
			h += yChange;
		}
		else if(yChange > 0){
			direction = "down";
			h += yChange;
		}

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

	public void portal(){
		if(direction == "up"){
			cy = 720;
		}
		else if(direction == "down"){
			cy = 210;
		}
		else if(direction == "left"){
			cx = 790;
		}
		else if(direction == "right"){
			cx = 50;
		}
	}


	//CHANGING DIRECTIONS
	public void moveRight(){
		xChange = 10;
		yChange = 0;

		w = 10;
		h = 10;
	}

	public void moveLeft(){
		xChange = -10;
		yChange = 0;

		w = 10;
		h = 10;
	}

	public void moveUp(){
		yChange = -10;
		xChange = 0;

		w = 10;
		h = 10;
	}

	public void moveDown(){
		yChange = 10;
		xChange = 0;

		w = 10;
		h = 10;
	}

	public void warpWall(){
		System.out.println("HI");
		wallWarp = true;
	}

	public void unWarpWall(){
		wallWarp = false;
	}
}