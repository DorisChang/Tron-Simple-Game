//Spot.java

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;

public class Spot{
	//private ArrayList<int> path = new ArrayList<int>();
	private int cx, cy; //current value;
	private int xChange, yChange;
	private Boolean hit = false;

	public Spot(int x, int y){
		cx = x;
		cy = y;
	}

	public boolean hitBorder(){
		if(xChange < 0){ //moving LEFT
			System.out.println("L");
			//System.out.println(cx);
			//System.out.println(cy);
			if(cx <= 0){
				hit = true;
				return true;
			}
		}

		if(xChange > 0){ //moving RIGHT
			System.out.println("R");
			//System.out.println(cx);
			//System.out.println(cy);
			if(cx + 10 >= 900){
				hit = true;
				return true;
			}
		}

		if(yChange < 0){ //moving UP
			System.out.println("U");
			//System.out.println(cx);
			//System.out.println(cy);
			if(cy <= 0){
				hit = true;
				return true;
			}
		}

		if(yChange > 0){ //moving DOWN
			System.out.println("D");
			//System.out.println(cx);
			System.out.println("CY:"+cy);
			if(cy + 10 >= 500){
				hit = true;
				return true;
			}
		}

		//else{
			hit = false;
			return false;
		//}
	}

	public int getX(){
		return cx;
	}

	public int getY(){
		return cy;
	}

	public void move(){
		cx += xChange;
		cy += yChange;

		//System.out.println(cx);
		System.out.println(cy);

		if(hitBorder() == true){
			xChange = 0;
			yChange = 0;
		}

		//System.out.println(hit);
		//System.out.println(hitBorder());
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