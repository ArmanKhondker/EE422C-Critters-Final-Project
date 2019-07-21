package assignment5;

import java.util.*;

/* Critters GUI
 * EE422C Project 5 submission by
 * Arman Khondker
 * aak2464
 * 16345
 * Alex Kim
 * atk595
 * 16380
 * Slip days used: <1>
 * Git URL: https://github.com/EE422C-Fall-2018/project-5-critters-2-project-5-pair-18
 * Fall 2018
 */


/* Written by Arman Khondker
 * 
 * Critter 1 walks in any random direction between right and left
 * Critter 1 wants to fight only if he is matched against Critter 2 or Critter 3
 * Critter 1 reproduces if its energy is greater than 15 with a 1/4 chance 
 * if he doesn't fight he walks left 
 * */
 

public class Critter1 extends Critter {

	private static int stepsMoved = 0;
	private static int critter3 = 0;
	
	/**
	 * Update the Critter
	 */
	@Override
	public void doTimeStep() {
		walk(getRandomInt(4));
		
		if (getEnergy() > 15 && getRandomInt(4) == 0) {
			Critter1 child = new Critter1();
			reproduce(child, Critter.getRandomInt(8));
		}
	}
	
	/**
	 * Determine whether or not the Critter fights
	 * @return true if it wants to fight
	 */
	@Override
	public boolean fight(String opponent) {
		if(opponent.equals("2") || opponent.equals("3"))
		{
			if(opponent.equals("3")) 
				critter3++;
			return true;   
		}
		else
		{
			stepsMoved++;
			walk(4);
			return false;
		}
	}
	
	/**
	 * Output what the critter should look like in the world
	 * @return "1"
	 */
	public String toString() {
		return "1";
	}
	
	/**
	 * Gets the Critter's stats
	 * @return String containing stats
	 */
	public static String runStats(java.util.List<Critter> critters) {
		String stats = critters.size() + " alive; " + stepsMoved + " steps moved; " + critter3 + " critter3 eaten";
		return stats;
	}
	
	/**
	 * Gets the shape of the Critter
	 * @return TRIANGLE
	 */
	@Override
	public CritterShape viewShape() {
		return CritterShape.TRIANGLE;
	}
	
	/**
	 * Gets the Color of the Critter
	 * @return beige
	 */
	@Override
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.BEIGE; 
	}
	
	/**
	 * Gets the Color of the Critter's outline
	 * @return fuchsia
	 */
	@Override
    public javafx.scene.paint.Color viewOutlineColor() {
        return  javafx.scene.paint.Color.FUCHSIA; 
    }
}