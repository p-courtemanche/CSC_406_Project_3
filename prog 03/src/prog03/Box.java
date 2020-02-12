package prog03;

import java.awt.Color;

import processing.core.*; 

/**	The Box class rewritten as a subclass of GraphicObject
 * 
 * @author jyh and Paige Courtemanche
 *
 */
public class Box extends GraphicObject {

	/**	Constructor. Initializes all instance variables to the values set by the arguments
	 * 
	 * @param x			x coordinate of the object's center (in world coordinates)
	 * @param y			y coordinate of the object's center (in world coordinates)
	 * @param angle		orientation of the object (in rad)
	 * @param width		width of the object (in world units)
	 * @param height	height of the object (in world units)
	 * @param red		the value of the red component for the object's color
	 * @param green     the value of the green component for the object's color
	 * @param blue      the value of the blue component for the object's color
	 * @param alpha     the opacity of the object
	 * @param vx		Horizontal component of the object's velocity vector (in world units per second)
	 * @param vy		Vertical component of the object's velocity vector (in world units per second)
	 * @param spin		Spin of the object (in rad/s)
	 */
	public Box(float x, float y, float angle, float width, float height, float red, float green, float blue, float alpha, float vx, float vy, float spin)
	{
		super(x, y, angle, width, height, red, green, blue, alpha, vx, vy, spin);
	}
	
	/**	Default constructor. Initializes all instance variables with random values.
	 */
	public Box() {
		super();
	}

	/** Checks whether a mouse click is on an object or not
	 * 
	 * @param x		The coordinate of a mouse click on the x-axis
	 * @param y		The coordinate of a mouse click on the y-axis
	 * @return 		boolean describing whether the mouse click is on an object or not
	 */
	public boolean isInside(float x, float y) {
		return (x <= (x_ + (width_/2))) && (x >= (x_ - (width_/2)))
				&& (y <= (y_ + (height_/2))) && (y >= (y_ - (height_/2)));
	}

	
	/**	Rendering code specific to boxes
	 * 
	 * @param app	The Processing application in which the action takes place
	 */
	protected void draw_(PApplet app) {
		app.translate(-width_/2,  -height_/2);
		app.rect(0,  0,  width_, height_);
	}
}