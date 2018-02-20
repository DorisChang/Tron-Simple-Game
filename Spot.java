//Spot.java

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.util.Random;

public class Spot{
	private int cx, cy; //current coordinates
	private int sx, sy; //coordinates at where the player changes directions
	private int w, h; //width and height of the rectangle
	private int xChange, yChange; //how much the x,y are changing for player
	private int deltaX, deltaY; //direction (either 0 or 1)
	private String direction; //direction the player is moving
	private boolean teleportPlayer, pressTurbo, shield, flippedControls; //powerUp booleans
	private int turboLeft; //# of turbos available
	//0 - extra turbo, 1 - portal, 2 - double speed, 3 - shield

	
	private Rectangle rect = new Rectangle(0,0,5,5);
	private ArrayList<java.awt.Rectangle>trail = new ArrayList<java.awt.Rectangle>(); //arraylist for trail

	//=========================Constructor==========================
	public Spot(int x, int y){
		cx = x;
		cy = y;

		pressTurbo = false;

		turboLeft = 3;

		teleportPlayer = false;
		shield = false;

		addSection();
	}

	//=====================Player Coords/Info======================

	public int returnX(){
		return cx;
	}

	public int returnY(){
		return cy;
	}

	public int getSX(){
		return sx;
	}

	public int getSY(){
		return sy;
	}

	public int width(){ //gives width of rectangle
		return sx-cx;
	}

	public int height(){ //gives height of rectangle
		return sy-cy;
	}

	//=======================Moving Player===========================
	public void move(){ //piece moving
		cx += xChange;
		cy += yChange;

		//==========changing the size of the trail rectangle==========
		if(xChange < 0){
			direction = "left";

			rect.setBounds(cx,cy,width(),height()+5);
		}
		else if(xChange > 0){
			direction = "right";

			rect.setBounds(sx,sy,-1*width()+5,height()+5);
		}
		else if(yChange < 0){
			direction = "up";

			rect.setBounds(cx,cy,width()+5,height());
		}
		else if(yChange > 0){
			direction = "down";

			rect.setBounds(sx,sy,width()+5,-1*height()+5);
		}
	}

	public void noMove(){ //stops player from moving
		xChange = 0;
		yChange = 0;
	}

	public String moveD(){ //current direction
		return direction;
	}

	//=====================Trail and Collisions=======================
	public ArrayList<Rectangle> returnTrail(){
		return trail;
	}

	//current rectangle being worked on (Starts a new rectangle)
	public void addSection(){
		rect = new Rectangle(cx,cy,5,5);
		trail.add(rect);
	}

	public boolean checkCollisions(ArrayList<Rectangle> otherTrail){
		if(shield == false){
			Rectangle pRect = new Rectangle(cx+5*deltaX,cy+5*deltaY,w,h);

			//if player hits the opponent's trail
			for(int i = 0; i < otherTrail.size(); i++){
				if(otherTrail.get(i).intersects(pRect)){
					return true;
				}
			}

			//if player hits its own trail
			for(int i = 0; i < trail.size()-1; i++){
				if(trail.get(i).intersects(pRect)){
					return true;
				}
			}
		}

		if(hitBorder()){
			return true;
		}

		return false;
	}

	//player hits the border
	public boolean hitBorder(){
		if(teleportPlayer == true){
			if(direction == "left" && cx <= 60){
				cx = 790;

				sx = cx;
				sy = cy;

				addSection();
			}

			else if(direction == "right" && cx >= 785){
				cx = 60;

				sx = cx;
				sy = cy;

				addSection();
			}

			else if(direction == "up" && cy <= 110){
				cy = 710;

				sx = cx;
				sy = cy;

				addSection();
			}

			else if(direction == "down" && cy >= 705){
				cy = 110;

				sx = cx;
				sy = cy;

				addSection();
			}

			return false;
		}

		else{ 
			//goes in one side, comes out the other
			if(cx <= 60){
				sx = cx;
				cx = 60; //so the rectangle will stop at the border

				return true;
			}

			else if(cx >= 785){
				rect.setBounds(sx,sy,(785-sx)+5,height()+5);//so the rectangle will stop at the border
				cx = 785;

				return true;
			}

			else if(cy <= 110){
				sy = cy;
				cy = 110;//so the rectangle will stop at the border

				return true;
			}

			else if(cy >= 705){
				rect.setBounds(sx,sy,width()+5,(705-sy)+5);//so the rectangle will stop at the border
				cy = 705;

				return true;
			}

			else{
				return false;
			}
		}
	}

	public boolean getPowerUp(Rectangle p){
		Rectangle pRect = new Rectangle(cx+5*deltaX,cy+5*deltaY,w,h);

		if(pRect.intersects(p)){
			return true;
		}

		return false;
	}

	//=========================Power Ups==============================
	//=====Turbo=====
	public void turbo(){
		if(turboLeft > 0){ //increase rate of change
			if(direction == "left"){
				xChange = -15;
			}

			else if(direction == "right"){
				xChange = 15;
			}

			else if(direction == "up"){
				yChange = -15;
			}

			else if(direction == "down"){
				yChange = 15;
			}

			pressTurbo = true;
		}
	}

	public void stopTurbo(){
		if(direction == "left"){ //bring rate back to normal
			xChange = -5;
		}

		else if(direction == "right"){
			xChange = 5;
		}

		else if(direction == "up"){
			yChange = -5;
		}

		else if(direction == "down"){
			yChange = 5;
		}

		turboLeft--;

		pressTurbo = false;

		if(turboLeft < 0){
			turboLeft = 0;
		}
	}

	public boolean turboSpeed(){ 
		//returns whether or not turbo has been pressed already
		return pressTurbo;
	}

	//======Extra Turbo=====
	public void extraTurbo(){ //gains a turbo
		if(turboLeft == 3){
			turboLeft = 3;
		}

		else{
			turboLeft += 1;
		}
	}

	public int countTurboLeft(){ //how many turbo left
		return turboLeft;
	}

	//=====Portal=====
	public void openPortal(){
		teleportPlayer = true;
	}

	public void closePortal(){
		teleportPlayer = false;
	}

	//=====Double Speed=====
	public void doubleSpeed(){ //doubles the speed
		if(direction == "left"){
			xChange = -10;
		}

		else if(direction == "right"){
			xChange = 10;
		}

		else if(direction == "up"){
			yChange = -10;
		}

		else if(direction == "down"){
			yChange = 10;
		}
	}

	public void normalSpeed(){ //speed back to normal
		if(direction == "left"){
			xChange = -5;
		}

		else if(direction == "right"){
			xChange = 5;
		}

		else if(direction == "up"){
			yChange = -5;
		}

		else if(direction == "down"){
			yChange = 5;
		}
	}

	//=====Sheild=====
	public void shieldUp(){
		shield = true;
	}

	public void shieldDown(){
		shield = false;
	}

	//=====Reverse=====
	public void reverseControls(){
		flippedControls = true;
	}

	public void revertControls(){
		flippedControls = false;
	}

	public boolean controlsReversed(){
		return flippedControls;
	}

	//=====Clear Trail=====
	public void emptyTrail(){
		trail.clear();
		sx = cx;
		sy = cy;

		addSection();
	}
	//======================Changing Direction=======================
	public void moveRight(){
		direction = "right";
		xChange = 5;
		yChange = 0;

		addSection();

		deltaX = 0;
		deltaY = 0;

		w = 10;
		h = 5;

		sx = cx;
		sy = cy;
	}

	public void moveLeft(){
		direction = "left";
		xChange = -5;
		yChange = 0;

		addSection();

		deltaX = -1;
		deltaY = 0;

		w = 10;
		h = 5;

		sx = cx;
		sy = cy;
	}

	public void moveUp(){
		direction = "up";
		yChange = -5;
		xChange = 0;

		addSection();
		
		deltaX = 0;
		deltaY = -1;

		w = 5;
		h = 10;

		sx = cx;
		sy = cy;
	}

	public void moveDown(){
		direction = "down";
		yChange = 5;
		xChange = 0;

		addSection();

		deltaX = 0;
		deltaY = 0;

		w = 5;
		h = 10;

		sx = cx;
		sy = cy;
	}
}