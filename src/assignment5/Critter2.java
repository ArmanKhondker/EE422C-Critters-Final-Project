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
 * Critter 2 runs in a random direction and walks south when a fight occurs
 * Critter 2 reproduces iff its energy is greater than 35
 * Critter 2 doesn't want to fight unless it is faced with an algae
 * */
 

public class Critter2 extends Critter {
	
	private static int stepsMoved = 0;
	private static int fights = 0;
	
	/**
	 * Update the Critter
	 */
	@Override
	public void doTimeStep() {
		run(getRandomInt(8));
		stepsMoved += 2;
		
		if (getEnergy() > 35) {
			Critter2 child = new Critter2();
			reproduce(child, Critter.getRandomInt(8));
		}
	}
	
	/**
	 * Determine whether or not the Critter fights
	 * @return true if it wants to fight
	 */
	@Override
	public boolean fight(String opponent) {
		stepsMoved++;
		walk(7);
		if (opponent.toString().equals("@")) {
			fights++;
			return true;
		}
		return false;
	}
	
	/**
	 * Output what the critter should look like in the world
	 * @return "2"
	 */
	public String toString() {
		return "2";
	}
	
	/**
	 * Gets the Critter's stats
	 * @return String containing stats
	 */
	public static String runStats(java.util.List<Critter> critters) {
		String stats = critters.size() + " alive; " + stepsMoved + " steps moved; " + fights + " algae eaten";
		return stats;
	}
	
	/**
	 * Gets the shape of the Critter
	 * @return DIAMOND
	 */
	@Override
	public CritterShape viewShape() {
		return CritterShape.DIAMOND;
	}
	
	/**
	 * Gets the Color of the Critter
	 * @return cyan
	 */
	@Override
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.CYAN; 
	}
	
	/**
	 * Gets the Color of the Critter's outline
	 * @return crimson
	 */
	@Override
    public javafx.scene.paint.Color viewOutlineColor() {
        return  javafx.scene.paint.Color.CRIMSON; 
    }
}