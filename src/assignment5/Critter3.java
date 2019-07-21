package assignment5;

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
 * Critter 3 walks in a random direction 1/3 of the time
 * If Critter 3 runs into another Critter 3, they both simply walk off
 * If Critter 3 runs into any other Critter, it attempts to run to an empty spot
 * If Critter 3 has over 60 energy, it reproduces two children
 * */

public class Critter3 extends Critter {

	private static int stepsMoved = 0;
	private static int algaeEaten = 0;
	
	/**
	 * Update the Critter
	 */
	@Override
	public void doTimeStep() {
		// Walk in a random direction with a 1/3 chance
		if (getRandomInt(3) == 0) {
			walk(getRandomInt(8));
			stepsMoved++;
		}
		
		// Reproduce to the left and right
		if (getEnergy() > 60) {
			Critter3 child1 = new Critter3();
			reproduce(child1, 0);
			Critter3 child2 = new Critter3();
			reproduce(child2, 4);
		}
	}
	
	/**
	 * Determine whether or not the Critter fights
	 * @return true if it wants to fight
	 */
	@Override
	public boolean fight(String opponent) {
		// If it's another Critter3, walk off
		if (opponent.toString().equals("3"))
			walk(getRandomInt(8));
		
		// Eat algae
		else if (opponent.toString().equals("@")) {
			algaeEaten++;
			return true;
		}
		
		// Otherwise, run away to an empty spot (if none are available, run up and left)
		else {
			int dir = 0;
			while (look(dir, true) != null && dir < 8) {
				dir++;
			}
			run(dir);
			stepsMoved += 2;
		}
		
		return false;
	}
	
	/**
	 * Gets the Critter's stats
	 * @return String containing stats
	 */
	public static String runStats(java.util.List<Critter> critters) {
		String stats = critters.size() + " alive; " + stepsMoved + " steps moved; " + algaeEaten + " algae eaten";
		return stats;
	}
	
	/**
	 * Output what the critter should look like in the world
	 * @return "3"
	 */
	@Override
	public String toString () {
		return "3";
	}
	
	/**
	 * Gets the shape of the Critter
	 * @return STAR
	 */
	@Override
	public CritterShape viewShape() {
		return CritterShape.STAR;
	}
	
	/**
	 * Gets the Color of the Critter
	 * @return yellow
	 */
	@Override
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.YELLOW; 
	}
	
	/**
	 * Gets the Color of the Critter's outline
	 * @return pink
	 */
	@Override
	public javafx.scene.paint.Color viewOutlineColor() {
		return javafx.scene.paint.Color.PINK; 
	}
}