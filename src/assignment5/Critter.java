package assignment5;

import java.awt.Polygon;

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

import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.*;


public abstract class Critter {
	/* NEW FOR PROJECT 5 */
	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR
	}
	
	/* the default color is white, which I hope makes critters invisible by default
	 * If you change the background color of your View component, then update the default
	 * color to be the same as you background 
	 * 
	 * critters must override at least one of the following three methods, it is not 
	 * proper for critters to remain invisible in the view
	 * 
	 * If a critter only overrides the outline color, then it will look like a non-filled 
	 * shape, at least, that's the intent. You can edit these default methods however you 
	 * need to, but please preserve that intent as you implement them. 
	 */
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.WHITE; 
	}
	
	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	public abstract CritterShape viewShape(); 
	
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private static boolean moved;
	private static int fightsWon = 0;
	

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	/**
	 * Make a Critter look 1 or 2 tiles in a single direction
	 * @param direction
	 * @param steps
	 * @return String of the Critter at the location
	 */
	protected final String look(int direction, boolean steps) {
		int x_look = 0, y_look = 0, spots = 1;
		if (steps)
			spots = 2;
		
		// Look in a single direction
		if (direction == 0)
			x_look += spots;
		else if (direction == 1) {
			x_look += spots;
			y_look -= spots;
		}
		else if (direction == 2)
			y_look -= spots;
		else if (direction == 3) {
			x_look -= spots;
			y_look -= spots;
		}
		else if (direction == 4)
			x_look -= spots;
		else if (direction == 5) {
			x_look -= spots;
			y_look += spots;
		}
		else if (direction == 6)
			y_look += spots;
		else if (direction == 7) {
			x_look += spots;
			y_look += spots;
		}
		
		// Account for moving out of boundaries
		if (x_look >= Params.world_width)
			x_look -= Params.world_width;
		if (x_look <= -1)
			x_look += Params.world_width;
		if (y_look >= Params.world_height)
			y_look -= Params.world_height;
		if (y_look <= -1)
			y_look += Params.world_height;
		
		this.energy -= Params.look_energy_cost;
		for (Critter c: population) {
			if (x_look == c.x_coord && y_look == c.y_coord)
				return c.toString();
		}
		return null;
	}
	
	/* rest is unchanged from Project 4 */
	
	
	private static java.util.Random rand = new java.util.Random();
	
	/**
	 * Get a random integer
	 * @param max
	 * @return a random integer
	 */
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	/**
	 * Set the seed of the world
	 * @param new_seed
	 */
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	
	/**
	 * Make a Critter move 1 tile in a single direction
	 * @param direction
	 */
	protected final void walk(int direction) {
		move(direction, 1);
		this.energy -= Params.walk_energy_cost;
		moved = true;
	}
	
	/**
	 * Make a Critter move 2 tiles in a single direction
	 * @param direction
	 */
	protected final void run(int direction) {
		move(direction, 2);
		this.energy -= Params.run_energy_cost;
		moved = true;
	}
	
	/**
	 * Create an offspring of a Critter with half the parent Critter's energy
	 * @param offspring
	 * @param direction
	 */
	protected final void reproduce(Critter offspring, int direction) {
		if(this.energy < Params.min_reproduce_energy) return;
		
		offspring.energy = this.energy / 2;  //set energy equal to half of parent 
		offspring.x_coord = this.x_coord;
		offspring.y_coord = this.y_coord;
		
		this.energy = this.energy / 2 + this.energy % 2;
		
        offspring.walk(direction);   //move in the direction passed in 
        babies.add(offspring);		// add offspring to babies list if spawned successfully
    }

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);

	/**
	 * Update the world
	 */
	public static void worldTimeStep() {
		for (int i = 0; i < population.size(); i++) {
			Critter c = population.get(i);
			
			// Move/reproduce
			moved = false;
			c.doTimeStep();
			
			for (int j = 0; j < population.size(); j++) {
				Critter o = population.get(j);
				if (!o.equals(c) && o.x_coord == c.x_coord && o.y_coord == c.y_coord && c.energy > 0 && o.energy > 0) {
					int cRoll = 0, oRoll = 0;
					
					if (c.fight(o.toString()) && o.fight(c.toString())) {
						if (c.energy > 0 && o.energy > 0) {
							cRoll = getRandomInt(c.energy);
							oRoll = getRandomInt(o.energy);
						}
					
						// Current critter wins
						if (cRoll >= oRoll) {
							c.energy += o.energy * 0.5;
							o.energy = 0;
						}
						// Other critter wins
						if (cRoll < oRoll) {
							o.energy += c.energy * 0.5;
							c.energy = 0;
						}
					}
					else if (c.toString().equals("@")) {
						o.energy += c.energy * 0.5;
						c.energy = 0;
						population.remove(c);
					}
					else if (o.toString().equals("@")) {
						c.energy += o.energy * 0.5;
						o.energy = 0;
						population.remove(o);
					}
				}
			}
			
			// Tick away energy from all critters
			c.energy -= Params.rest_energy_cost;
			
			// Remove dead critters
			if (c.energy <= 0) {
				population.remove(c);
			}
		}
		
		// Refresh algae
		try {
			for (int i = 0; i < Params.refresh_algae_count; i++)
				Critter.makeCritter("Algae");
		} catch (InvalidCritterException e) {
			e.printStackTrace();
		}
		
		// Add babies to population
		for (Critter b: babies)
			population.add(b);
		babies.clear();
	}

	/**
	 * Displays the world
	 */
	public static void displayWorld() {
		GridPane grid = Main.world;
		
		grid.getChildren().clear();

		// Delete any out-of-bound Critters (after decreasing world size)
		List<Critter> outOfBounds = new java.util.ArrayList<Critter>();
		for (int i = 0; i < population.size(); i++) {
			if (population.get(i).x_coord >= Params.world_width || population.get(i).y_coord >= Params.world_height)
				outOfBounds.add(population.get(i));
		}
		for (Critter c: outOfBounds)
			population.remove(c);
		
		// Draw the grid
		for (int i = 0; i < Params.world_width; i++) {
			for (int j = 0; j < Params.world_height; j++) {
				Shape s = new Rectangle(((1024 / Params.world_width) - 2) * Main.scale, ((576 / Params.world_height) - 2) * Main.scale);
				s.setFill(null);
				if (Main.gridShow.isSelected())
					s.setStroke(Color.CORNFLOWERBLUE);
				else
					s.setStroke(Color.ALICEBLUE);
				grid.add(s,  i,  j);
			}
		}
		
		// Draw the Critters, based on shape
		for (Critter c: population) {
			Shape s = new Rectangle(1, 1);
			
			if (c.viewShape() == CritterShape.SQUARE) {
				s = new Rectangle(((1024 / Params.world_width) - 2) * Main.scale, ((576 / Params.world_height) - 2) * Main.scale);
			}
			if (c.viewShape() == CritterShape.CIRCLE) {
				int radius = (int)(((1024 / Params.world_width) - 2) / 2 * Main.scale);
				if ((1024 / Params.world_width) > (576 / Params.world_height))
					radius = (int)(((576 / Params.world_height) - 2) / 2 * Main.scale);
				s = new Circle(radius);
			}
		
			if (c.viewShape() == CritterShape.DIAMOND) {
				Diamond d = new Diamond(((1024 / Params.world_width) - 2) * Main.scale, ((576 / Params.world_height) - 2) * Main.scale);
				d.polygonDiamond().setFill(c.viewFillColor());
				d.polygonDiamond().setStroke(c.viewOutlineColor());
				grid.add(d.polygonDiamond(),  c.x_coord,  c.y_coord);
				
			}
			
			if (c.viewShape() == CritterShape.TRIANGLE) {
				Triangle tri = new Triangle(((1024 / Params.world_width) - 2) * Main.scale, ((576 / Params.world_height) - 2) * Main.scale);
				tri.polygonTriangle().setFill(c.viewFillColor());
				tri.polygonTriangle().setStroke(c.viewOutlineColor());
				grid.add(tri.polygonTriangle(),  c.x_coord,  c.y_coord);
				
			}
			
			if (c.viewShape() == CritterShape.STAR) {
				Star star = new Star(((1024 / Params.world_width) - 2) * Main.scale, ((576 / Params.world_height) - 2) * Main.scale);
				star.polygonStar().setFill(c.viewFillColor()); 
				star.polygonStar().setStroke(c.viewOutlineColor());
				grid.add(star.polygonStar(),  c.x_coord,  c.y_coord);
			}
			
			s.setFill(c.viewFillColor()); 
			s.setStroke(c.viewOutlineColor());
			
			grid.add(s,  c.x_coord,  c.y_coord);
		}
	}

	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		Class<?> classOfCritter;
		try {
			classOfCritter = Class.forName(myPackage + "." + critter_class_name);  //formatting need for forName function 
		}
		catch (ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		}
		if (Critter.class.isAssignableFrom(classOfCritter)) {
			try {
				Critter newCrit = (Critter)classOfCritter.newInstance();
				newCrit.energy = Params.start_energy;
				newCrit.x_coord = getRandomInt(Params.world_width);
				newCrit.y_coord = getRandomInt(Params.world_height);
				population.add(newCrit);
			}
			catch (Exception e) {
				//e.printStackTrace();
				throw new InvalidCritterException(critter_class_name);
			}
		}}

	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		Class<?> classOfCritter; 
		
		try {
			classOfCritter = Class.forName(myPackage + "." + critter_class_name);  //formatting need for forName function 
		}
		catch (ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		}
		if (Critter.class.isAssignableFrom(classOfCritter)) {
			try {
				Critter checkClass = (Critter)classOfCritter.newInstance();
				String type = checkClass.toString();
				
				for (Critter c: population) {
					if (c.toString().equals(type))
						result.add(c);
				}
			}
			catch (Exception e) {
				throw new InvalidCritterException(critter_class_name);
			}
		}
		return result;
	}

	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static String runStats(List<Critter> critters) {
		
		String stats = critters.size() + " alive; ";
		return stats;
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure thath the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctup update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}
	
	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		population.clear();
	}
	
	/**
	 * Update a Critter's position
	 * @param dir
	 * @param spots
	 */
	private void move(int dir, int spots) {
		// Only move if not moved yet this time step
		if (!moved) {
			// Move in a single, random direction
			if (dir == 0)
				x_coord += spots;
			else if (dir == 1) {
				x_coord += spots;
				y_coord -= spots;
			}
			else if (dir == 2)
				y_coord -= spots;
			else if (dir == 3) {
				x_coord -= spots;
				y_coord -= spots;
			}
			else if (dir == 4)
				x_coord -= spots;
			else if (dir == 5) {
				x_coord -= spots;
				y_coord += spots;
			}
			else if (dir == 6)
				y_coord += spots;
			else if (dir == 7) {
				x_coord += spots;
				y_coord += spots;
			}
			
			// Account for moving out of boundaries
			if (x_coord >= Params.world_width)
				x_coord -= Params.world_width;
			if (x_coord <= -1)
				x_coord += Params.world_width;
			if (y_coord >= Params.world_height)
				y_coord -= Params.world_height;
			if (y_coord <= -1)
				y_coord += Params.world_height;
		}
	}
}
