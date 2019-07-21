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

public class Star {
	private Polygon poly;
	private double b;
	private double h;

	public Star(double base, double height)
	{
		this.poly = new Polygon();
		this.b = base - 2;
		this.h = height - 2;
		poly.getPoints().addAll(new Double[]{b/8, h, b/6, 2*h/3, 0.1, h/3, b/3, h/3, b/2, 0.1, 2*b/3, h/3, b, h/3, 5*b/6, 2*h/3, 7*b/8, h, b/2, 5*h/6});
	}

	public Polygon polygonStar() {
		return this.poly;
	}



}
