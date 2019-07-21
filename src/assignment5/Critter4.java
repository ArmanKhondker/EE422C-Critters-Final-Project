package assignment5;

import assignment5.Critter;

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


/* Written by Alex Kim
 * 
 * Critter 4 attempts to move into a spot containing a Critter 1
 * If no Critter1 is identified, Critter 4 always moves along the row to the right
 * Critter 4 will attempt to fight all other Critters
 * If Critter 4 has over 100 energy, it reproduces a child into the row above
 * */

public class Critter4 extends Critter {

	private static int stepsMoved = 0;
	private static int fights = 0;
	
	/**
	 * Update the Critter
	 */
	@Override
	public void doTimeStep() {
		int dir = getRandomInt(8);
		if (look(dir, false) != null && look(dir, false).equals("1")) {
			walk(dir);
			stepsMoved++;
		} else {
			walk(0);
			stepsMoved++;
		}
		
		// Reproduce into the row above (don't want competitors)
		if (getEnergy() > 100) {
			Critter4 child = new Critter4();
			reproduce(child, 2);
		}
	}
	
	/**
	 * Determine whether or not the Critter fights
	 * @return true if it wants to fight
	 */
	@Override
	public boolean fight(String opponent) {
		// Always fight
		fights++;
		return true;
	}
	
	/**
	 * Gets the Critter's stats
	 * @return String containing stats
	 */
	public static String runStats(java.util.List<Critter> critters) {
		String stats = critters.size() + " alive; " + stepsMoved + " steps moved; " + fights + " fights fought";
		return stats;
	}
	
	/**
	 * Output what the critter should look like in the world
	 * @return "4"
	 */
	@Override
	public String toString () {
		return "4";
	}
	
	/**
	 * Gets the shape of the Critter
	 * @return CIRCLE
	 */
	@Override
	public CritterShape viewShape() {
		return CritterShape.CIRCLE;
	}
	
	/**
	 * Gets the Color of the Critter
	 * @return orange
	 */
	@Override
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.ORANGE; 
	}
	
}