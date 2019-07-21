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

package assignment5;

import javafx.scene.shape.*;

public class Triangle {
	
	private Polygon poly;
    private double b;
    private double h;

	public Triangle(double base, double height)
	{
		this.poly = new Polygon();
		this.b = base - 2;
		this.h = height - 2; 
	    poly.getPoints().addAll(new Double[]{b/2, 0.1, b, h, 0.1, h});
	}
	
    public Polygon polygonTriangle() {
        return this.poly;
    }

}

